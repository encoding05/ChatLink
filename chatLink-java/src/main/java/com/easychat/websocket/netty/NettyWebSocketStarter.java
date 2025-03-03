package com.easychat.websocket.netty;

import com.easychat.entity.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketStarter implements Runnable{

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketStarter.class);

    @Resource
    private AppConfig appConfig;

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            // 对http协议的支持，使用http的编码器 解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest/FullHttpContent
                            // 保证接收的 Http 请求的完整性
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            // 心跳 long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit
                            // readerIdleTime: 读超时时间 测试端一定时间内未读到数据
                            // writerIdleTime：写超时时间 0表示不启用
                            // allIdleTime：所有类型的超时时间
                            pipeline.addLast(new IdleStateHandler(60*60*24, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new HandlerHeartBeat());
                            // 将协议升级为ws协议，对websocket的支持
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 1024 * 64, true, true, 10000L));
                            pipeline.addLast(handlerWebSocket);
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(appConfig.getWsPort()).sync();
            logger.info("netty启动成功， 端口：{}", appConfig.getWsPort());
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("启动netty失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}

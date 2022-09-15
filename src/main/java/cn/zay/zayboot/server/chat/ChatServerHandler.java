package cn.zay.zayboot.server.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ZAY
 * 聊天室服务端的
 */
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * GlobalEventExecutor.INSTANCE 是一个全局的事件执行器, 是一个单例
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 当一个客户端上线时要干的事
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //将该客户端加入聊天群的消息推送给其他的客户端 ,该方法会将 channelGroup中所有的 channel遍历, 并推送消息
        CHANNEL_GROUP.writeAndFlush("[客户端] "+channel.remoteAddress()+" 上线了 "+dateFormat.format(new Date())+"\n");
        //将当前新登录的客户端加入到 channelGroup中
        CHANNEL_GROUP.add(channel);
        log.info(channel.remoteAddress()+"上线了!\n");
    }
    /**
     * 当一个客户端处于不活动状态时, 提示离线
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        //将该客户端加入聊天群的消息推送给其他的客户端 ,该方法会将 channelGroup中所有的 channel遍历, 并推送消息
        CHANNEL_GROUP.writeAndFlush("[客户端] "+channel.remoteAddress()+" 离线了 "+dateFormat.format(new Date())+"\n");
        //将当前新登录的客户端从 channelGroup中删除
        CHANNEL_GROUP.remove(channel);
    }
    /**
     * 当服务器接收到一个客户端发来的消息时, 将消息分发到 channelGroup中的每一个客户端
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        //获取到当前发送消息的客户端的 channel
        Channel channel = ctx.channel();
        for (Channel channel1 : CHANNEL_GROUP) {
            if(channel == channel1){
                //如果是当前发消息的 channel
                channel1.writeAndFlush("[自己]发送了消息:"+msg+"\n");
            }else {
                //不是当前发送消息的 channel
                channel1.writeAndFlush("[客户端]"+channel.remoteAddress()+"发送了消息:"+msg+"\n");
            }
        }
    }
}

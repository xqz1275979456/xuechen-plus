package com.xuecheng.messagesdk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.messagesdk.mapper.MqMessageHistoryMapper;
import com.xuecheng.messagesdk.mapper.MqMessageMapper;
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.model.po.MqMessageHistory;
import com.xuecheng.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements MqMessageService {

    @Autowired
    MqMessageMapper mqMessageMapper;

    @Autowired
    MqMessageHistoryMapper mqMessageHistoryMapper;


    /**
     * 扫描消息表记录，采用与扫描视频处理表相同的思路
     *
     * @param shardIndex  分片序号
     * @param shardTotal  分片总数
     * @param messageType 消息类型
     * @param count       扫描记录数
     * @return {@link List}<{@link MqMessage}> 消息记录
     */
    @Override
    public List<MqMessage> getMessageList(int shardIndex, int shardTotal, String messageType,int count) {
        return mqMessageMapper.selectListByShardIndex(shardTotal,shardIndex,messageType,count);
    }

    /**
     * 添加消息
     *
     * @param messageType  消息类型
     * @param businessKey1 业务id
     * @param businessKey2 业务id
     * @param businessKey3 业务id
     * @return {@link MqMessage}  消息内容
     */
    @Override
    public MqMessage addMessage(String messageType, String businessKey1, String businessKey2, String businessKey3) {
        MqMessage mqMessage = new MqMessage();
        mqMessage.setMessageType(messageType);
        mqMessage.setBusinessKey1(businessKey1);
        mqMessage.setBusinessKey2(businessKey2);
        mqMessage.setBusinessKey3(businessKey3);
        int insert = mqMessageMapper.insert(mqMessage);
        if(insert>0){
            return mqMessage;
        }else{
            return null;
        }

    }

    /**
     * 完成任务
     *
     * @param id 消息id
     * @return 更新成功 1
     */
    @Transactional
    @Override
    public int completed(long id) {
        MqMessage mqMessage = new MqMessage();
        //完成任务
        mqMessage.setState("1");
        int update = mqMessageMapper.update(mqMessage, new LambdaQueryWrapper<MqMessage>().eq(MqMessage::getId, id));
        if(update>0){

            mqMessage = mqMessageMapper.selectById(id);
            //添加到历史表
            MqMessageHistory mqMessageHistory = new MqMessageHistory();
            BeanUtils.copyProperties(mqMessage,mqMessageHistory);
            mqMessageHistoryMapper.insert(mqMessageHistory);
            //删除消息表
            mqMessageMapper.deleteById(id);
            return 1;
        }
        return 0;

    }
    /**
     * 完成阶段任务
     *
     * @param id 消息id
     * @return int 更新成功：1
     */
    @Override
    public int completedStageOne(long id) {
        MqMessage mqMessage = new MqMessage();
        //完成阶段1任务
        mqMessage.setStageState1("1");
        return mqMessageMapper.update(mqMessage,new LambdaQueryWrapper<MqMessage>().eq(MqMessage::getId,id));
    }

    @Override
    public int completedStageTwo(long id) {
        MqMessage mqMessage = new MqMessage();
        //完成阶段2任务
        mqMessage.setStageState2("1");
        return mqMessageMapper.update(mqMessage,new LambdaQueryWrapper<MqMessage>().eq(MqMessage::getId,id));
    }

    @Override
    public int completedStageThree(long id) {
        MqMessage mqMessage = new MqMessage();
        //完成阶段3任务
        mqMessage.setStageState3("1");
        return mqMessageMapper.update(mqMessage,new LambdaQueryWrapper<MqMessage>().eq(MqMessage::getId,id));
    }

    @Override
    public int completedStageFour(long id) {
        MqMessage mqMessage = new MqMessage();
        //完成阶段4任务
        mqMessage.setStageState4("1");
        return mqMessageMapper.update(mqMessage,new LambdaQueryWrapper<MqMessage>().eq(MqMessage::getId,id));
    }

    /**
     * 查询阶段状态
     *
     * @param id id
     * @return int
     */
    @Override
    public int getStageOne(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState1());
    }

    @Override
    public int getStageTwo(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState2());
    }

    @Override
    public int getStageThree(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState3());
    }

    @Override
    public int getStageFour(long id) {
        return Integer.parseInt(mqMessageMapper.selectById(id).getStageState4());
    }


}

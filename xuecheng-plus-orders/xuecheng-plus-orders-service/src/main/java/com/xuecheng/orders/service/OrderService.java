package com.xuecheng.orders.service;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.orders.model.dto.AddOrderDto;
import com.xuecheng.orders.model.dto.PayRecordDto;
import com.xuecheng.orders.model.dto.PayStatusDto;
import com.xuecheng.orders.model.po.XcPayRecord;

/**
 * @author unbroken
 * @Description 订单相关的service接口
 * @Version 1.0
 * @date 2023/4/5 17:42
 */
public interface OrderService {

    /**
     * 创建商品订单
     * @param userId
     * @param addOrderDto 订单信息
     * @return 支付记录(包括二维码)
     */
    public PayRecordDto createOrder(String userId, AddOrderDto addOrderDto);

    /**
     * 查询支付记录
     * @param payNo 交易记录号
     * @return
     */
    public XcPayRecord getPayRecordByPayno(String payNo);

    /**
     * 请求支付宝查询支付结果
     * @param payNo 支付记录id
     * @return 支付记录信息
     */
    public PayRecordDto queryPayResult(String payNo);

    /**
     * 保存支付状态
     * @param payStatusDto
     */
    public void saveAliPayStatus(PayStatusDto payStatusDto);

    /**
     * 发送通知结果
     * @param mqMessage
     */
    public void notifyPayResult(MqMessage mqMessage);
}

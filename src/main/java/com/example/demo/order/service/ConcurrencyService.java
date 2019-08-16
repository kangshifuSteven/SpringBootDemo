package com.example.demo.order.service;

import com.example.demo.order.dao.ProductDao;
import com.example.demo.order.dao.ProductRobbingRecordDao;
import com.example.demo.order.domain.Product;
import com.example.demo.order.domain.ProductRobbingRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Created by Administrator on 2018/8/25.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConcurrencyService {

    private static final Logger log= LoggerFactory.getLogger(ConcurrencyService.class);

    private static final String ProductNo="product_10010";

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductRobbingRecordDao productRobbingRecordDao;

    /**
     * 处理抢单
     * @param mobile
     */
    public void manageRobbing(String mobile){
        /*try {
            Product product=productMapper.selectByProductNo(ProductNo);
            if (product!=null && product.getTotal()>0){
                log.info("当前手机号：{} 恭喜您抢到单了!",mobile);
                productMapper.updateTotal(product);
            }else{
                log.error("当前手机号：{} 抢不到单!",mobile);

            }
        }catch (Exception e){
            log.error("处理抢单发生异常：mobile={} ",mobile);
        }*/ //--v1.0


        //+v2.0
        try {
            Product product=productDao.selectByProductNo(ProductNo);
            if (product!=null && product.getTotal()>0){
                int result=productDao.updateTotal(product);
                if (result>0) {
                    ProductRobbingRecord entity=new ProductRobbingRecord();
                    entity.setMobile(mobile);
                    entity.setProductId(product.getId());
                    productRobbingRecordDao.insertSelective(entity);
                }
            }
            //int a  = 1/0;
        }catch (Exception e){
            log.error("处理抢单发生异常：mobile={} ",mobile);
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw  new RuntimeException(e.getMessage());
        }
    }
}


















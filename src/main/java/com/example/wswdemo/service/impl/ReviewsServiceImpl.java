package com.example.wswdemo.service.impl;

import com.example.wswdemo.pojo.entity.Reviews;
import com.example.wswdemo.mapper.ReviewsMapper;
import com.example.wswdemo.service.IReviewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评价表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class ReviewsServiceImpl extends ServiceImpl<ReviewsMapper, Reviews> implements IReviewsService {

}

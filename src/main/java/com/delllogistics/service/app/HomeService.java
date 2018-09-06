package com.delllogistics.service.app;

import com.delllogistics.entity.goods.Goods;
import com.delllogistics.entity.goods.GoodsSeries;
import com.delllogistics.entity.goods.GoodsTag;
import com.delllogistics.entity.sys.Company;
import com.delllogistics.repository.sys.CompanyRepository;
import com.delllogistics.repository.goods.GoodsSeriesRepository;
import com.delllogistics.repository.goods.GoodsTagRepository;
import com.delllogistics.service.goods.GoodsService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * app首页
 * Created by calvin  2017/11/22
 */
@Service
public class HomeService {

    private final GoodsTagRepository goodsTagRepository;
    private final GoodsSeriesRepository goodsSeriesRepository;
    private final GoodsService goodsService;
    private final CompanyRepository companyRepository;

    public HomeService(GoodsTagRepository goodsTagRepository, GoodsSeriesRepository goodsSeriesRepository, GoodsService goodsService, CompanyRepository companyRepository) {
        this.goodsTagRepository = goodsTagRepository;
        this.goodsSeriesRepository = goodsSeriesRepository;
        this.goodsService = goodsService;
        this.companyRepository = companyRepository;
    }

    public Page<Goods> findMainGoods(int page, int size, Long companyId, String type) {

        Company company = companyRepository.findOne(companyId);
        if(company==null){
            return null;
        }



        //需要优化
        GoodsTag goodsTag = goodsTagRepository.findByName(type);
        if (goodsTag != null) {
            //List<GoodsTag> goodsTagList =  goodsTagRepository.findAll(specification);
            Set<GoodsTag> goodsTags=new HashSet<>();
            goodsTags.add(goodsTag);
            Goods goods = new Goods();
            goods.setGoodsTags(goodsTags);
            goods.setCompany(company);
            return goodsService.findAll(page, size, goods);
        }
        return null;
    }


    public Page<Goods> findGoodsBySeries(int page, int size, Long companyId, Long seriesId) {
        Company company = companyRepository.findOne(companyId);
        if(company==null){
            return null;
        }
        Goods goods = new Goods();
        goods.setCompany(company);
        //设置系列
        if(seriesId!=null){
            GoodsSeries goodsSeries =  goodsSeriesRepository.findOne(seriesId);
            goods.setGoodsSeries(goodsSeries);
        }
        return goodsService.findAll(page, size, goods);
    }



}

package com.tz.serviceImpl;

import com.tz.mapper.BgmMapper;
import com.tz.pojo.Bgm;
import com.tz.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class BgmServiceImpl implements BgmService {

    @Autowired
    BgmMapper bgmMapper;

    @Override
    public List<Bgm> queryBgmList() {
        List<Bgm> bgms = bgmMapper.selectAll();
        return bgms;
    }
}

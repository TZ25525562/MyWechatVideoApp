package com.tz.service;

import com.tz.pojo.Bgm;
import java.util.List;

/**
 * 背景音乐相关接口
 */
public interface BgmService {

    /**
     * 查询bgm列表
     * @param
     * @return
     */
    List<Bgm> queryBgmList();

}

package com.tz.mapper;

import com.tz.pojo.Videos;
import com.tz.pojo.VideosExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VideosMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    long countByExample(VideosExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int deleteByExample(VideosExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int insert(Videos record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int insertSelective(Videos record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    List<Videos> selectByExample(VideosExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    Videos selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int updateByExampleSelective(@Param("record") Videos record, @Param("example") VideosExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int updateByExample(@Param("record") Videos record, @Param("example") VideosExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int updateByPrimaryKeySelective(Videos record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table videos
     *
     * @mbg.generated Sun Dec 06 14:06:29 CST 2020
     */
    int updateByPrimaryKey(Videos record);
}
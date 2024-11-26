<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="net.musecom.community.mapper.BbsMapper">
  
  <!-- 게시물 삽입 -->
  <insert id="insertBbs" parameterType="bbs" useGeneratedKeys="true" keyProperty="id">
     INSERT INTO bbs (bbsid, ref, title, writer, password, userid, content, sec, category)
     VALUES (
       #{bbsid}, #{id}, #{title}, #{writer}, #{password}, #{userid}, #{content}, #{sec}, #{category}
     )
  </insert>
  <!--  ref 업데이트 -->
  <update id="refUpdateById" parameterType="map">
     UPDATE bbs 
     SET ref = #{ref}
     WHERE id=#{id}
  </update>
  
  <!-- 게시물 비번 확인 -->
  <select id="bbsByIdAndPassword" parameterType="map" resultType="int">
     select count(*) from bbs where id=#{id} and password=#{password}
  </select>
  
  
  <!-- 답글 삽입 -->
  
  <!-- 게시물 수정 -->
  <update id="updateBbs" parameterType="bbs">
     UPDATE bbs SET 
       title = #{title},
       writer = #{writer},
       content = #{content},
       sec=#{sec},
       category=#{category}
     WHERE id=#{id}
  </update>
  
  <!-- 게시물 삭제 -->
  <delete id="deleteBbs" parameterType="long">
    DELETE FROM bbs WHERE id = #{id}
  </delete>
  
  
  <!-- 전체 목록 갯수 조회 --> 
  <select id="selectCountBbs" parameterType="int" resultType="int">
    SELECT count(*) FROM bbs where bbsid=#{bbsid}
  </select>
  
  <!-- 검색 게시물 전체 목록 조회 -->
    <select id="selectSearchCountBbs" parameterType="map" resultType="int">
    SELECT count(*) FROM bbs 
    where bbsid=#{bbsid} 
    AND ${key} LIKE CONCAT('%' , #{val}, '%')
  </select>
  
  <!-- 게시물 페이지에 따른 조회 -->
  <select id="selectBbs" parameterType="map" resultType="bbs">
    SELECT * FROM bbs
    WHERE bbsid=#{bbsid}
    ORDER BY ref DESC, step ASC
    LIMIT #{page}, #{recordsPerPage}
  </select>
  
  <!-- 검색 조회 -->
  <select id="selectSearchBbs" parameterType="map" resultType="bbs">
    SELECT * FROM bbs
    WHERE bbsid=#{bbsid} AND ${key} LIKE CONCAT('%' , #{val}, '%')
    ORDER BY ref DESC, step ASC
    LIMIT #{page}, #{recordsPerPage}
  </select>
  
  <!--  조회수 카운트 -->
  <update id="updateHit">
     UPDATE bbs SET hit = hit + 1 WHERE id=#{id}
  </update>
  
  <!--  상세 조회 -->
 <select id="viewBbs" resultType="bbs">
    Select * from bbs where id=#{id}
 </select>
  
  
  <!-- 메인페이지 전체 게시물 가져오기 -->
  <select id="selectMainLatestPosts" resultType="map">
    <![CDATA[
     SELECT 
        ba.id as bbsadmin_id,
        ba.bbstitle,
        ba.skin,
        b.id as bbs_id,
        b.title,
        b.writer,
        b.wdate,
        b.category,
        b.hit,
        b.nn
     FROM 
     bbs_admin ba
     LEFT JOIN ( 
        SELECT * FROM (
            SELECT id, bbsid, title, writer, wdate, category, hit,
                 ROW_NUMBER() OVER (partition by bbsid order by wdate desc) AS nn
            FROM bbs
       ) AS subquery 
       WHERE nn <=6
    ) b ON ba.id = b.bbsid  
    ORDER BY ba.id ASC, b.wdate DESC
    ]]>
  </select>
  
  <!--  검색 결과 bbsid 별로 가져오기 -->
  <select id="searchBbsPostsGrouped" parameterType="map" resultType="bbs">
     <![CDATA[
        SELECT
          b.id, b.bbsid, b.title, b.writer, b.wdate, b.category, b.hit, ba.bbstitle
        FROM
          bbs b
        LEFT JOIN
          bbs_admin ba on b.bbsid = ba.id
        WHERE
          b.title LIKE CONCAT('%', #{searchVal}, '%')
          OR b.content LIKE CONCAT('%', #{searchVal}, '%')
        ORDER BY
          b.bbsid asc, b.wdate desc
     ]]>
  </select>
  
  <!--  인기검색어 등록 -->
  <insert id="insertSearchKeyword" parameterType="map">
     INSERT INTO search_keyword
     (keyword, search_count)
     VALUES
     (#{keyword}, 1)
     ON DUPLICATE KEY UPDATE search_count = search_count + 1
  </insert>
  
  
  <!-- 인기검색어 조회 -->
  <select id="selectPopularKeywords" resultType="map">
     SELECT keyword, SUM(search_count) AS search_count
     FROM search_keyword
     GROUP BY keyword
     ORDER BY search_count DESC
     LIMIT 20;
  </select>
</mapper>
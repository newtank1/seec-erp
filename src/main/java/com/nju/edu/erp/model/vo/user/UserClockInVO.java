package com.nju.edu.erp.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserClockInVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String userName;


    /**
     * 打卡日期
     */
    private Date date;

}

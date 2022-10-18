package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.BankAccountVO;

import java.util.List;

public interface AccountService {

    /**
     * 创建一个账户
     * @param accountVO 账户内容
     * @return 包含id的账户
     */
    BankAccountVO create(BankAccountVO accountVO);

    /**
     * 删除账户
     * @param id 删除账户id
     */
    void deleteByID(Integer id);

    /**
     * 修改账户
     * @param accountVO 新账户内容
     */
    void update(BankAccountVO accountVO);

    /**
     * 查找账户
     * @param keyword 关键字
     * @return 包含keyword的账户
     */
    List<BankAccountVO> search(String keyword);

    BankAccountVO findById(Integer id);

    List<BankAccountVO> findAll();
}

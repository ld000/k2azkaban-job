package com.k2data.platform.nvr.domain;

import java.util.List;

/**
 * 获取接口设备信息的类
 * @author chenjingsi
 * @version 2016-05-09
 */
public class NvrMachineContrast {
    
    private String status;
    private List<NvrMachineContrastContent> content;
    
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<NvrMachineContrastContent> getContent() {
        return content;
    }
    public void setContent(List<NvrMachineContrastContent> content) {
        this.content = content;
    }
    
}
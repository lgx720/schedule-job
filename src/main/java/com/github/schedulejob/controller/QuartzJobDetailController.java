package com.github.schedulejob.controller;

import com.google.common.collect.Lists;
import com.github.schedulejob.common.Response;
import com.github.schedulejob.common.RetCodeConst;
import com.github.schedulejob.domain.job.JobDetailDO;
import com.github.schedulejob.service.QuartzJobDetailService;
import com.github.schedulejob.util.PageBuilder;
import com.github.schedulejob.util.ResponseBuilder;
import io.swagger.annotations.*;
import org.quartz.JobKey;
import org.quartz.core.jmx.JobDataMapSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * quartz api
 *
 * @author: lvhao
 * @since: 2016-6-23 20:18
 */
@Api("quartz 任务API")
@RestController
@RequestMapping("/jobs")
public class QuartzJobDetailController {

    @Autowired
    private QuartzJobDetailService quartzJobDetailService;

    @ApiOperation("获取任务列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "index",value = "页码",required = true,dataType = "int",paramType = "query"),
        @ApiImplicitParam(name = "size",value = "页大小",required = true, dataType = "int", paramType = "query")
    })
    @ApiResponses({
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @GetMapping
    public Response<List<JobDetailDO>> list(){
        List<JobDetailDO> jobDetailDOs = quartzJobDetailService.queryJobList();
        return ResponseBuilder.newResponse()
                .wittPage(PageBuilder.DEFAULT_PAGE_INFO)
                .withRetCode(RetCodeConst.OK)
                .withData(jobDetailDOs)
                .build();
    }

    @ApiOperation("查询指定jobKey jobDetail")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="path",name="group",value="组名",dataType = "String"),
        @ApiImplicitParam(paramType="path",name="name",value="名称",dataType = "String")
    })
    @ApiResponses({
        @ApiResponse(code=200, message = "",response = Response.class)
    })
    @GetMapping("/{group}/{name}")
    public Response<JobDetailDO> queryByJobKey(
            @PathVariable String name,
            @PathVariable String group){
        JobKey jobKey = new JobKey(name,group);
        JobDetailDO jobDetailDO = quartzJobDetailService.queryByKey(jobKey);
        return ResponseBuilder.newResponse()
                .withRetCode(RetCodeConst.OK)
                .withData(jobDetailDO)
                .build();
    }

    @ApiOperation("添加任务Job")
    @ApiImplicitParam(name = "jobDetailDO", value = "任务jobDetail", required = true, dataType = "JobDetailDO", paramType = "body")
    @ApiResponses({
        @ApiResponse(code = 200,message = "xxxx")
    })
    @PostMapping
    public Response add(@RequestBody JobDetailDO jobDetailDO){
        boolean result = quartzJobDetailService.add(jobDetailDO);
        return ResponseBuilder.newResponse()
                .withRetCodeBy(result)
                .build();
    }

    @ApiOperation("批量删除Job")
    @ApiImplicitParams({
        @ApiImplicitParam()
    })
    @DeleteMapping
    public Response delete(@RequestBody Map<String,List<String>> jobKeyGroups){
        List<JobKey> jobKeys = Lists.newArrayList();
        jobKeyGroups.forEach((k,v) ->
            v.forEach(name -> {
                JobKey jobKey = new JobKey(name,k);
                jobKeys.add(jobKey);
            })
        );
        boolean result = quartzJobDetailService.remove(jobKeys);
        return ResponseBuilder.newResponse()
                .withRetCodeBy(result)
                .build();
    }

    @ApiOperation("立即触发任务")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", name = "group", value = "组名", dataType = "String"),
        @ApiImplicitParam(paramType = "path", name = "name", value = "任务名", dataType = "String")
    })
    @ApiResponses({
        @ApiResponse(code=200,message="",response = Response.class),
        @ApiResponse(code=200,message="",response = Response.class)
    })
    @PostMapping("/{group}/{name}")
    public Response triggerNow(@PathVariable String group,
                              @PathVariable String name,
                              @RequestBody Map<String,Object> jobData){
        JobKey jobKey = new JobKey(name,group);
        boolean result = quartzJobDetailService.triggerNow(
            jobKey,
            JobDataMapSupport.newJobDataMap(jobData)
        );
        return ResponseBuilder.newResponse()
                .withRetCodeBy(result)
                .build();
    }
}
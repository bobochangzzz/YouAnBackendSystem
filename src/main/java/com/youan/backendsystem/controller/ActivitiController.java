package com.youan.backendsystem.controller;

import com.youan.backendsystem.common.BaseResponse;
import com.youan.backendsystem.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @Author: zz
 * @CreateTime: 2023-05-09  14:21
 * @Description: 流程管理模块
 * @Version: 1.0
 */
@RestController
@RequestMapping("/activiti")
@Slf4j
public class ActivitiController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;


    //添加流程定义通过上传bpmn
    @PostMapping("/uploadStreamAndDeployment")
    public BaseResponse uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile,
                                                  @RequestParam("processName") String processName) {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();
        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = multipartFile.getInputStream();
            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);
            // ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();//创建处理引擎实例
            // repositoryService = processEngine.getRepositoryService();//创建仓库服务实例
            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()//初始化流程
                        .addZipInputStream(zip)
                        .name(processName)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()//初始化流程
                        .addInputStream(fileName, fileInputStream)
                        .name(processName)
                        .deploy();
            }
            return ResultUtils.success("部署流程成功:" + deployment.getId());
        } catch (Exception e) {
            return ResultUtils.error(1, "上传文件失败");
        }
    }

    //添加流程定义通过在线提交BPMN的XML
    @PostMapping("/addDeploymentByString")
    public BaseResponse addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN,
                                              @RequestParam("processName") String processName) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)//第一个参数：资源名称，第二个参数：XML文件
                    .name(processName)
                    .deploy();
            return ResultUtils.success("部署流程成功:" + deployment.getId());
        } catch (Exception e) {
            return ResultUtils.error(1, "部署失败");
        }
    }


    //获取流程定义XML
    @GetMapping("/getDefinitionXML")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            e.toString();
        }
    }

    //获取流程定义列表
    @GetMapping("/getDefinitions")
    public BaseResponse getDefinitions() {

        try {
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

//            list.sort((y,x)->x.getVersion()-y.getVersion());

            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("processDefinitionID", pd.getId());
                hashMap.put("name", pd.getName());
                hashMap.put("key", pd.getKey());
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentID", pd.getDeploymentId());
                hashMap.put("version", pd.getVersion());
                listMap.add(hashMap);
            }

            return ResultUtils.success(listMap);

        } catch (Exception e) {
            return ResultUtils.error(1, "获取流程定义失败");
        }
    }


    //获取流程部署列表
    @GetMapping("/getDeployments")
    public BaseResponse getDeployments() {
        try {

            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
                listMap.add(hashMap);
            }
            return ResultUtils.success(listMap);
        } catch (Exception e) {
            return ResultUtils.error(1, "获取流程部署失败");
        }
    }


    //删除流程定义
    @GetMapping("/delDefinition")
    public BaseResponse delDefinition(@RequestParam("depID") String depID) {
        try {

            //删除数据
//            int result = mapper.DeleteFormData(pdID);

            repositoryService.deleteDeployment(depID, false);
            return ResultUtils.success("删除成功");

        } catch (Exception e) {
            return ResultUtils.error(1, "删除失败");
        }
    }


    //查询流程实例
    @GetMapping("/getInstances")
    public BaseResponse getInstances() {

        try {

            List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();

            //list.sort((y,x)->x.getProcessDefinitionVersion()-y.getProcessDefinitionVersion());
//            list.sort((y,x)->x.getStartDate().toString().compareTo(y.getStartDate().toString()));

            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
//                hashMap.put("processInstanceId", pi.getProcessInstanceId());
                hashMap.put("name", pi.getName());
                hashMap.put("deploymentId", pi.getDeploymentId());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartTime());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("resourceName", pd.getResourceName());
                listMap.add(hashMap);
            }

            return ResultUtils.success(listMap);
        } catch (Exception e) {
            return ResultUtils.error(1, "获取流程实例失败");
        }

    }

    //启动流程实例
    @GetMapping("/startProcess")
    public BaseResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                     @RequestParam("instanceName") String instanceName) {

        try {
            ProcessInstance processInstance = runtimeService.
                    startProcessInstanceByKey(processDefinitionKey, instanceName);
            return ResultUtils.success("启动流程实例成功：" + processInstance.getProcessInstanceId());
        } catch (Exception e) {
            return ResultUtils.error(1, "启动流程实例失败");
        }
    }


    //挂起流程实例

    //激活流程实例

    //删除流程实例

    //查询流程参数


}

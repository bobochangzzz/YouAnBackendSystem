package com.youan.backendsystem.controller;

import com.youan.backendsystem.common.BaseResponse;
import com.youan.backendsystem.common.ResultUtils;
import com.youan.backendsystem.common.TableDataInfo;
import com.youan.backendsystem.model.entity.Process;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;

import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * @Author: zz
 * @CreateTime: 2023-05-09  14:21
 * @Description: 部署管理接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/flow")
@Slf4j
public class FlowController {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    ProcessEngineConfiguration configuration;



    //上传一个工作流文件，流程部署
    @PostMapping(value = "/uploadworkflow")
    public BaseResponse fileupload(@RequestParam("processFile") MultipartFile uploadfile,
                                   @RequestParam("processName") String processName ) {
        try {
            String filename = uploadfile.getOriginalFilename();
            InputStream is = uploadfile.getInputStream();
            if (filename.endsWith("zip")) {
                repositoryService.createDeployment().name(filename)
                        .addZipInputStream(new ZipInputStream(is))
                        .name(processName)
                        .deploy();
            } else if (filename.endsWith("bpmn") || filename.endsWith("xml")) {
                repositoryService.createDeployment().name(filename)
                        .addInputStream(filename, is)
                        .name(processName)
                        .deploy();
            } else {
                return ResultUtils.error(1,"文件格式错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(1,"部署失败");
        }
        return ResultUtils.success("部署成功");
    }


    //查询已部署工作流列表
    @PostMapping(value = "/getprocesslists")
    public TableDataInfo getlist(@RequestParam(required = false) String key, @RequestParam(required = false) String name,
                                 Integer pageSize, Integer pageNum) {
        ProcessDefinitionQuery queryCondition = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotEmpty(key)) {
            queryCondition.processDefinitionKey(key);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryCondition.processDefinitionName(name);
        }

        int total = queryCondition.list().size();
        int start = (pageNum - 1) * pageSize;
        List<ProcessDefinition> pageList = queryCondition.orderByDeploymentId().desc().listPage(start, pageSize);
        List<Process> mylist = new ArrayList<Process>();
        for (int i = 0; i < pageList.size(); i++) {
            Process p =new Process();
            p.setDeploymentId(pageList.get(i).getDeploymentId());
            p.setId(pageList.get(i).getId());
            p.setKey(pageList.get(i).getKey());
            p.setName(pageList.get(i).getName());
            p.setResourceName(pageList.get(i).getResourceName());
            p.setDiagramresourceName(pageList.get(i).getDiagramResourceName());
            p.setVersion(pageList.get(i).getVersion());
            p.setSuspended(pageList.get(i).isSuspended());
            mylist.add(p);
        }
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(mylist);
        rspData.setTotal(total);
        return rspData;
    }

    //根据部署id删除已部署的工作流
    @PostMapping(value = "/removeDeploymentId")
    public BaseResponse removeDeploymentId(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return ResultUtils.success("删除成功");
    }

    //查看工作流图片
    @GetMapping(value = "/showresource")
    public void showresource(@RequestParam("pdid") String pdid,
                             HttpServletResponse response) throws Exception {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pdid);
        ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
        InputStream is = diagramGenerator.generateDiagram(bpmnModel, "png",  "宋体", "宋体", "宋体", configuration.getClassLoader(), 1.0);
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(is, output);
    }


    //挂起一个流程定义
    @GetMapping(value = "/suspendProcessDefinition")
    public BaseResponse suspendProcessDefinition(@RequestParam("pdid") String pdid) throws Exception {

            repositoryService.suspendProcessDefinitionById(pdid);

        return ResultUtils.success("挂起流程定义："+pdid+"成功");
    }

    //激活一个流程定义
    @GetMapping(value = "/activateProcessDefinition")
    public BaseResponse activateProcessDefinition(@RequestParam("pdid") String pdid) throws Exception {

            repositoryService.activateProcessDefinitionById(pdid);

        return ResultUtils.success("激活流程定义："+pdid+"成功");
    }

//
//    //添加流程定义通过在线提交BPMN的XML
//    @PostMapping("/addDeploymentByString")
//    public BaseResponse addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN,
//                                              @RequestParam("processName") String processName) {
//        try {
//            Deployment deployment = repositoryService.createDeployment()
//                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)//第一个参数：资源名称，第二个参数：XML文件
//                    .name(processName)
//                    .deploy();
//            return ResultUtils.success("部署流程成功:" + deployment.getId());
//        } catch (Exception e) {
//            return ResultUtils.error(1, "部署失败");
//        }
//    }
//
//
//    //获取流程定义XML
//    @GetMapping("/getDefinitionXML")
//    public void getProcessDefineXML(HttpServletResponse response,
//                                    @RequestParam("deploymentId") String deploymentId,
//                                    @RequestParam("resourceName") String resourceName) {
//        try {
//            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
//            int count = inputStream.available();
//            byte[] bytes = new byte[count];
//            response.setContentType("text/xml");
//            OutputStream outputStream = response.getOutputStream();
//            while (inputStream.read(bytes) != -1) {
//                outputStream.write(bytes);
//            }
//            inputStream.close();
//        } catch (Exception e) {
//            e.toString();
//        }
//    }
//
//    //获取流程定义列表
//    @GetMapping("/getDefinitions")
//    public BaseResponse getDefinitions() {
//
//        try {
//            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
//            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
//
////            list.sort((y,x)->x.getVersion()-y.getVersion());
//
//            for (ProcessDefinition pd : list) {
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("processDefinitionID", pd.getId());
//                hashMap.put("name", pd.getName());
//                hashMap.put("key", pd.getKey());
//                hashMap.put("resourceName", pd.getResourceName());
//                hashMap.put("deploymentID", pd.getDeploymentId());
//                hashMap.put("version", pd.getVersion());
//                listMap.add(hashMap);
//            }
//
//            return ResultUtils.success(listMap);
//
//        } catch (Exception e) {
//            return ResultUtils.error(1, "获取流程定义失败");
//        }
//    }
//
//
//    //获取流程部署列表
//    @GetMapping("/getDeployments")
//    public BaseResponse getDeployments() {
//        try {
//
//            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
//            List<Deployment> list = repositoryService.createDeploymentQuery().list();
//            for (Deployment dep : list) {
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("id", dep.getId());
//                hashMap.put("name", dep.getName());
//                hashMap.put("deploymentTime", dep.getDeploymentTime());
//                listMap.add(hashMap);
//            }
//            return ResultUtils.success(listMap);
//        } catch (Exception e) {
//            return ResultUtils.error(1, "获取流程部署失败");
//        }
//    }
//
//
//    //删除流程定义
//    @GetMapping("/delDefinition")
//    public BaseResponse delDefinition(@RequestParam("depID") String depID) {
//        try {
//
//            //删除数据
////            int result = mapper.DeleteFormData(pdID);
//
//            repositoryService.deleteDeployment(depID, false);
//            return ResultUtils.success("删除成功");
//
//        } catch (Exception e) {
//            return ResultUtils.error(1, "删除失败");
//        }
//    }
//
//
//    //查询流程实例
//    @GetMapping("/getInstances")
//    public BaseResponse getInstances() {
//
//        try {
//
//            List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
//
//            //list.sort((y,x)->x.getProcessDefinitionVersion()-y.getProcessDefinitionVersion());
////            list.sort((y,x)->x.getStartDate().toString().compareTo(y.getStartDate().toString()));
//
//            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
//            for (ProcessInstance pi : list) {
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("id", pi.getId());
////                hashMap.put("processInstanceId", pi.getProcessInstanceId());
//                hashMap.put("name", pi.getName());
//                hashMap.put("deploymentId", pi.getDeploymentId());
//                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
//                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
//                hashMap.put("startDate", pi.getStartTime());
//                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
//                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
//                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
//                        .processDefinitionId(pi.getProcessDefinitionId())
//                        .singleResult();
//                hashMap.put("resourceName", pd.getResourceName());
//                listMap.add(hashMap);
//            }
//
//            return ResultUtils.success(listMap);
//        } catch (Exception e) {
//            return ResultUtils.error(1, "获取流程实例失败");
//        }
//
//    }
//
//    //启动流程实例
//    @GetMapping("/startProcess")
//    public BaseResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
//                                     @RequestParam("instanceName") String instanceName) {
//
//        try {
//            ProcessInstance processInstance = runtimeService.
//                    startProcessInstanceByKey(processDefinitionKey, instanceName);
//            return ResultUtils.success("启动流程实例成功：" + processInstance.getProcessInstanceId());
//        } catch (Exception e) {
//            return ResultUtils.error(1, "启动流程实例失败");
//        }
//    }
//
//
//    //挂起流程实例
//    @GetMapping("/suspendInstance")
//    public BaseResponse suspendInstance(@RequestParam("instanceID") String instanceID) {
//
//        try {
//            runtimeService.suspendProcessInstanceById(instanceID);
//            return ResultUtils.success("挂起流程实例成功");
//        } catch (Exception e) {
//            return ResultUtils.error(1, "挂起流程实例失败");
//        }
//    }
//
//    //激活流程实例
//    @GetMapping("/resumeInstance")
//    public BaseResponse resumeInstance(@RequestParam("instanceID") String instanceID) {
//
//        try {
//            runtimeService.activateProcessInstanceById(instanceID);
//            return ResultUtils.success("激活流程实例成功");
//        } catch (Exception e) {
//            return ResultUtils.error(1, "激活挂起流程实例失败");
//        }
//    }
//
//    //删除流程实例
//    @GetMapping("/deleteInstance")
//    public BaseResponse deleteInstance(@RequestParam("instanceID") String instanceID) {
//        try {
//            runtimeService.deleteProcessInstance(instanceID, "删除流程实例");
//            return ResultUtils.success("删除流程实例成功");
//        } catch (Exception e) {
//            return ResultUtils.error(1, "删除流程实例失败");
//        }
//    }
//
//    //查询流程参数
////    @GetMapping("/variables")
////    public BaseResponse variables(@RequestParam("instanceID") String instanceID) {
////        try {
////
////            Map<String, VariableInstance> variableInstances = runtimeService.getVariableInstances(instanceID);
////
////            return ResultUtils.success(variableInstances);
////        }
////        catch(Exception e)
////        {
////            return ResultUtils.error(1, "查询流程参数失败");
////        }
////    }
//
//    //获取所有任务，管理员权限
//    @GetMapping(value = "/getTasks")
//    public BaseResponse getTasks() {
//        try {
//
//            List<Task> tasks = taskService.createTaskQuery().list();
//            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
//
//            for (Task tk : tasks) {
//                ProcessInstance processInstance =
//                        runtimeService.createProcessInstanceQuery()
//                                .processInstanceId(tk.getProcessInstanceId()).singleResult();
//                HashMap<String, Object> hashMap = new HashMap<>();
//                hashMap.put("id", tk.getId());
//                //任务节点名称
//                hashMap.put("name", tk.getName());
//                hashMap.put("createdDate", tk.getCreateTime());
//                if (tk.getAssignee() == null) {//执行人，null时前台显示未拾取
//                    hashMap.put("assignee", "待拾取任务");
//                } else {
//                    hashMap.put("assignee", tk.getAssignee());
//                }
//                //流程实例名称
//                hashMap.put("instanceName",processInstance.getName());
//                listMap.add(hashMap);
//            }
//            return ResultUtils.success(listMap);
//        } catch (Exception e) {
//            return ResultUtils.error(1, "获取任务失败");
//        }
//    }
//
//
//    //完成待办任务
//    @GetMapping("/completeTask")
//    public BaseResponse completeTask(@RequestParam("taskID") String taskID) {
//        try {
//
////            Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
//
////            if (task.getAssignee() == null) {
////                taskService.claim(taskID,);
////
////            }
//            taskService.complete(taskID);
//            return ResultUtils.success("完成任务成功");
//        } catch (Exception e) {
//            return ResultUtils.error(1, "完成任务失败");
//        }
//    }
//
//    //用户历史任务
//    @GetMapping("/getInstancesByUserName")
//    public BaseResponse getInstancesByUserName() {
//        try {
//
//            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
//                    .orderByHistoricTaskInstanceEndTime().asc()
//                    .taskAssignee("bajie")//要改成动态
//                    .list();
//
//          return ResultUtils.success(historicTaskInstances);
//        } catch (Exception e) {
//            return ResultUtils.error(1, "获取用户历史任务失败");
//        }
//
//    }
//
//    //根据流程实例ID查询已经执行过的流程实例任务
//    //在流程实例页面中调用
//    @GetMapping("/getInstancesByPiID")
//    public BaseResponse getInstancesByPiID(@RequestParam("piID") String piID) {
//        try {
//
//            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
//                    .orderByHistoricTaskInstanceEndTime().asc()
//                    .processInstanceId(piID)
//                    .list();
//
//            return ResultUtils.success(historicTaskInstances);
//        } catch (Exception e) {
//            return ResultUtils.error(1, "查询流程历史任务失败");
//        }
//
//    }
//    //渲染动态表单
//
//    //保存动态表单
//
//    //高亮显示流程历史




}

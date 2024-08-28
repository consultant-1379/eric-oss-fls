package eric.oss.fls.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import eric.oss.fls.repository.filter.CustomRSQLVisitor;
import eric.oss.fls.repository.model.Instrumentation;
import eric.oss.fls.repository.model.NodeInfo;

/**
 * @author xjaimah
 *
 */
@RestController
@RequestMapping("/")
public class FileLookupServiceController {

    /**
     * Counter
     */
    public static int CONSTANT = 1;

    /**
     * The NodeInfoRepository
     */
  private final NodeInfoRepository repository;

    /**
     * The instRepository
     */
  private final InstrumentationRepository instRepository;

    /**
     * The logger
     */
  final Logger logger = LogManager.getLogger(FileLookupServiceController.class);

    /**
     * IP used to redirect login request
     */
  private String IP;
    /**
     * This is used to save id parameter which already send to FNS
     */
  private String FNS_ID = "0";
  private boolean isPipeline = false;
  private static Map<String, Boolean> nodeMap = new HashMap<String, Boolean>();

  private int expectedRadioNodeCount, expectedPccCount, expectedPcgCount, expectedLteCtrCount, expectedNrCtrCount,
  expectedEbsnCount, expectedCnfCount = 0;

    /**
     * @param repository
     * @throws IOException
     * @throws FileNotFoundException
     */
  public FileLookupServiceController(NodeInfoRepository repository, InstrumentationRepository instRepository)
    throws FileNotFoundException, IOException {
    this.repository = repository;
    this.instRepository = instRepository;
    final Properties properties = new Properties();
    properties.load(new FileInputStream("/eric-oss-enm-fls/config/application.properties"));
    IP = properties.getProperty("spring.service.ip");
    if (null != properties.getProperty("fns.id")) {
      FNS_ID = properties.getProperty("fns.id");
    }
    if (null != properties.getProperty("pipeline")) {
      if (properties.getProperty("pipeline").equalsIgnoreCase("true")) {
        isPipeline = true;
      }
    }
    expectedRadioNodeCount = Integer.parseInt(properties.getProperty("expectedRadioNodeCount"));
    expectedPccCount = Integer.parseInt(properties.getProperty("expectedPccCount"));
    expectedPcgCount = Integer.parseInt(properties.getProperty("expectedPcgCount"));
    expectedLteCtrCount = Integer.parseInt(properties.getProperty("expectedLteCtrCount"));
    expectedNrCtrCount = Integer.parseInt(properties.getProperty("expectedNrCtrCount"));
    expectedEbsnCount = Integer.parseInt(properties.getProperty("expectedEbsnCount"));
    expectedCnfCount = Integer.parseInt(properties.getProperty("expectedCnfCount"));
  }

    /**
     * @return
     */
  @GetMapping("/file/v1/getAllFiles")
  List<NodeInfo> getAll() {
    try {
      List<NodeInfo> val = repository.findAll();
      return val;
    } catch (Exception ex) {
      logger.error("Exception occur while fecthing data from data base please verify DB is configure properly.");
    }
    return null;
  }
  @GetMapping("/file/v1/files")
  public Map<String, List<Object>> response(@RequestParam(value = "filter") String search,
                                            @RequestParam(value = "select", required = false) String select,
                                            @RequestParam(value = "limit", required = false) String limit,
                                            @RequestParam(value = "offset", required = false) String offset,
                                            @RequestParam(value = "orderBy", required = false) String orderBy) {
    logger.info("Request coming from FNS: {}", search);
    // dataType==PM_STATISTICAL;nodeType==RadioNode;id=gt=
    // workaround
    // System.out.println("Pipeline is set to : " + isPipeline);
    String dataType = "";
    if (search.contains(";id=gt=") && isPipeline) {
      dataType = search.split("dataType==")[1].split(";")[0];
      if (search.contains("nodeType==")) {
        dataType = dataType + "," + search.split("nodeType==")[1].split(";")[0];
      } else {
        dataType = dataType + ",";
      }
      // System.out.println("Initial nodeMap: " + nodeMap.keySet());
      // System.out.println("Current dataType is " + dataType);
      if (!nodeMap.containsKey(dataType)) {
        nodeMap.put(dataType, true);
        String searchId = search.split("id=gt=")[1];
        search = search.replace("id=gt=" + searchId, "id=gt=0");
        logger.info("Search query has been changed to 0 for node_type: {}", dataType);
      }
    }
    Map<String, List<Object>> responseObj = new HashMap<String, List<Object>>();
    List<Object> responseList = new ArrayList<Object>();
    Map<String, Object> finalResponseMap = new LinkedHashMap<String, Object>();
    Node rootNode = null;
    rootNode = new RSQLParser().parse(search);
    Specification<NodeInfo> spec = rootNode.accept(new CustomRSQLVisitor<NodeInfo>());
    List<NodeInfo> responseObject = new ArrayList<>(repository.findAll(spec));
    List<NodeInfo> realObject = removeDuplicate(responseObject);
    if (null != select) {
      String selectFields[] = select.split(",");
      if (selectFields.length == 1 && selectFields[0].equalsIgnoreCase("id")) {
        // finalResponseMap.put("id", 0L);
        getMaxRecord(realObject, finalResponseMap, responseList);
        // responseList.add(responseObject);
      } else {
        getSelectedRecord(realObject, responseList, selectFields, search);
      }
    } else {
      responseList.addAll(realObject);
    }
    if (search.contains(";id=gt=") && isPipeline) {
      if (dataType.equals("PM_STATISTICAL,RadioNode")) {
        if (responseList.size() != expectedRadioNodeCount) { // 10500) {
          // Only if true value for key dataType
          // System.out.println(dataType + " value is: " + nodeMap.get(dataType));
          if (nodeMap.get(dataType)) {
            // System.out.println("Removed map.");
            nodeMap.remove(dataType);
            // System.out.println(nodeMap.keySet());
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_STATISTICAL,PCC")) {
        if (responseList.size() != expectedPccCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_STATISTICAL,PCG")) {
        if (responseList.size() != expectedPcgCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_STATISTICAL,Shared-CNF")) {
        if (responseList.size() != expectedCnfCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_CELLTRACE_*,")) {
        if (responseList.size() != expectedNrCtrCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_EBSN_*,RadioNode")) {
        if (responseList.size() != expectedEbsnCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
      if (dataType.equals("PM_CELLTRACE,")) {
        if (responseList.size() != expectedLteCtrCount) {
          if (nodeMap.get(dataType)) {
            nodeMap.remove(dataType);
          }
          logger.info("Actual records prepared for FNS {}: {}", search, responseList.size());
          responseList.clear();
        } else {
          nodeMap.put(dataType, false);
        }
      }
    }
    responseObj.put("files", responseList);
    logger.info("Number of Record Prepare for FNS: {}", responseList.size());
    return responseObj;
  }

    /**
     * @param responseObject
     * @param finalResponseMap
     * @param responseList
     */
  public void getMaxRecord(List<NodeInfo> responseObject, Map<String, Object> finalResponseMap,
                           List<Object> responseList) {
    if (responseObject.size() != 0) {
      NodeInfo element = Collections.max(responseObject, Comparator.comparingLong(NodeInfo::getId));
      finalResponseMap.put("id", element.getId());
      responseList.add(finalResponseMap);
      logger.info("Response to FNS for record id : {} for node type : {}", element.getId(),
                  element.getNodeType());
    }else {
      finalResponseMap.put("id", 0);
      responseList.add(finalResponseMap);
      // responseList.add(finalResponseMap);
      logger.info("Response to FNS for record id : 0");
    }
  }
  public List<NodeInfo> removeDuplicate(List<NodeInfo> responseObject) {
    if (FNS_ID.equalsIgnoreCase("-1")) {
      Map<String, Long> fileLocationMap = new HashMap<String, Long>();
      List<NodeInfo> responseRealObject = new ArrayList<NodeInfo>();
      for (NodeInfo node : responseObject) {
        if (!fileLocationMap.containsKey(node.getFileLocation())) {
          responseRealObject.add(node);
          fileLocationMap.put(node.getFileLocation(), node.getId());
        } else {
          logger.warn(
            "WARN: Duplicate Record Found with id : {} same entry present inside DB with id : {} File Location: {}",
            node.getId(), fileLocationMap.get(node.getFileLocation()), node.getFileLocation());
        }
      }
      return responseRealObject;
    } else {
      return responseObject;
    }
  }

    /**
    * This method will prepare response object with selected fields
    *
    * @param responseObject
    * @param finalResponseMap
    * @param responseList
    */
  public void getSelectedRecord(List<NodeInfo> responseObject, List<Object> responseList, String[] selectFields,
                                String search) {
    try {
      for (NodeInfo node : responseObject) {
        Map<String, Object> actualMap = new LinkedHashMap<String, Object>();
        for (String column : selectFields) {
          switch (column) {
            case "id":
              actualMap.put("id", node.getId());
              break;
            case "dataType":
              actualMap.put("dataType", node.getDataType());
              break;
            case "nodeName":
              actualMap.put("nodeName", node.getNodeName());
              break;
            case "nodeType":
              actualMap.put("nodeType", node.getNodeType());
              break;
            case "fileLocation":
              actualMap.put("fileLocation", node.getFileLocation());
              break;
            case "fileSize":
              actualMap.put("fileSize", node.getFileSize());
              break;
            case "startRopTimeInOss":
              actualMap.put("startRopTimeInOss", node.getStartRopTimeInOss());
              break;
            case "fileCreationTimeInOss":
              actualMap.put("fileCreationTimeInOss", node.getFileCreationTimeInOss());
              break;
            case "endRopTimeInOss":
              actualMap.put("endRopTimeInOss", node.getEndRopTimeInOss());
              break;
          }
        }
        // logger.info("Response to FNS for record id : {} for node type : {}",
        // node.getId(),node.getFileSize());
        responseList.add(actualMap);
      }
    } catch (Exception ex) {
      logger.error("ERROR: Unable to convert response object into JSON {}", ex.getMessage());
    }
  }
    /**
     * @param nodes
     * @return
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
  @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
  @ResponseBody
  public String updateDB(@RequestBody List<NodeInfo> nodes) throws JsonMappingException, JsonProcessingException {
    repository.saveAll(nodes);
    logger.info("json entries transferred complete to fls db, count of new files: {}, new max id: {}", nodes.size(), repository.findAll().size());
    return "Node DB updated sucessfully..";
  }

    /*
    * @param nodes
    *
    * @return
    *
    * @throws JsonMappingException
    *
    * @throws JsonProcessingException
    */
  @PostMapping(path = "/login")
  @ResponseBody
  public void login(HttpServletResponse httpResponse, HttpServletRequest request) {
    logger.info("Login request coming from FNS Service.");
    httpResponse.setHeader("Location", "http://" + IP + "/redirect");
    Cookie cc = new Cookie("iPlanetDirectoryPro",
                           "S1~AQIC5wM2LY4SfczW300mq8ytNoPX3wybmhUDqqp7eraBrMU.*AAJTSQACMDIAAlNLABM3MDY2OTcwNjU4NjM1MjU4NDMwAAJTMQACMDE.*");
    httpResponse.addCookie(cc);
    httpResponse.setStatus(302);
    logger.info("HTTP Response Object with Response code 302 : Redirecting the request.");
  }
    /**
     * @param httpResponse
     * @return
     * @throws IOException
     */
  @GetMapping(path = "/redirect")
  @ResponseBody
  public String redirect(HttpServletResponse httpResponse, HttpServletRequest request) throws IOException {
    logger.info("Login successful with FNS service with request {}", request.toString());
    logger.debug("{\"code\":\"SUCCESS\",\"message\":\"Authentication Successful\"}");
    Cookie cc = new Cookie("iPlanetDirectoryPro",
                           "S1~AQIC5wM2LY4SfczW300mq8ytNoPX3wybmhUDqqp7eraBrMU.*AAJTSQACMDIAAlNLABM3MDY2OTcwNjU4NjM1MjU4NDMwAAJTMQACMDE.*");
    httpResponse.addCookie(cc);
    httpResponse.setStatus(302);
    return "{\"code\":\"SUCCESS\",\"message\":\"Authentication Successful\"}";
  }
    /**
    * @param nodes
    * @return
    * @throws JsonMappingException
    * @throws JsonProcessingException
    */
  @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
  @ResponseBody
  public String deleteRecords(@RequestBody List<String> nodes) throws JsonMappingException, JsonProcessingException {
    logger.info("Inserted Delete method");
    for (String node : nodes) {
      try {
        logger.info("Inserted Delete method loop");
        String filter = "endRopTimeInOss==" + node;
        Node rootNode = new RSQLParser().parse(filter);
        Specification<NodeInfo> spec = rootNode.accept(new CustomRSQLVisitor<NodeInfo>());
        List<NodeInfo> nodeList = repository.findAll(spec);
        repository.deleteAll(nodeList);
      } catch (Exception ex) {
        logger.error("Error Occur during deleting records from DB");
      }
    }
    return "Records deleted sucessfully..";
  }
    /**
    * @param nodes
    * @return
    * @throws JsonMappingException
    * @throws JsonProcessingException
    */
  @PostMapping(path = "/addInstrumentation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
  @ResponseBody
  public String addInstrumentation(@RequestBody List<Instrumentation> nodes)
    throws JsonMappingException, JsonProcessingException {
    instRepository.saveAll(nodes);
    return "Node DB updated sucessfully..";
  }

    /**
    * @param nodes
    * @return
    * @throws JsonMappingException
    * @throws JsonProcessingException
    */
  @PostMapping(path = "/updateInstrumentation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
  @ResponseBody
  public String updateInstrumentation(@RequestBody List<String> nodes)
    throws JsonMappingException, JsonProcessingException {
    logger.info("Inserted Delete method");
    for (String node : nodes) {
      try {
        logger.info("Inserted Delete method loop");
        String filter = "endRopTimeInOss==" + node;
        Node rootNode = new RSQLParser().parse(filter);
        Specification<Instrumentation> spec = rootNode.accept(new CustomRSQLVisitor<Instrumentation>());
        List<Instrumentation> nodeList = instRepository.findAll(spec);
        for (Instrumentation nodeDel : nodeList) {
          instRepository.delete(nodeDel);
        }
      } catch (Exception ex) {
        logger.error("Error Occur during deleting records from DB");
      }
    }
    return "Records deleted sucessfully..";
  }

}
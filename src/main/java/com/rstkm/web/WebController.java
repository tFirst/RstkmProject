package com.rstkm.web;

import com.rstkm.bean.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import org.xml.sax.ContentHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/request")
public class WebController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody
    String getAnswer(@RequestParam Map<String, String> queryMap) throws JAXBException, IOException {
        RequestDetail requestDetail = new RequestDetail();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        ClientInfo clientInfo = new ClientInfo();
        if (request.getHeader("x-forwarded-for") != null) {
            clientInfo.setIpAddress(request.getHeader("x-forwarded-for"));
        } else {
            clientInfo.setIpAddress((request.getRemoteAddr()));
        }
        clientInfo.setUserAgent(request.getHeader("user-agent"));

        List<Parameter> numParam = new ArrayList<>();
        List<Parameter> strParam = new ArrayList<>();

        Object keys[] = queryMap.keySet().toArray();

        Parameter parameter;

        for (int i = 0; i < queryMap.size(); i++) {
            String param = queryMap.get(keys[i]);
            parameter = new Parameter();
            parameter.setName((String) keys[i]);
            parameter.setParameter(param);

            if (checkString(param, ".")) {
                numParam.add(parameter);
            } else {
                if (checkString(param, ",")) {
                    numParam.add(parameter);
                } else {
                    strParam.add(parameter);
                }
            }
        }

        Parameters parameters = new Parameters();
        parameters.setMethod(request.getMethod());
        parameters.setNumericParameters(numParam);
        parameters.setStringParameters(strParam);

        requestDetail.setClientInfo(clientInfo);
        requestDetail.setParameters(parameters);

        String filePath = "src/main/resources/page.xml";
        convertObjectToXml(requestDetail, filePath);

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        char[] arr = new char[(int) new File(filePath).length()];

        br.read(arr);

        return String.valueOf(arr);
    }

    private static void convertObjectToXml(RequestDetail requestDetail, String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(RequestDetail.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(requestDetail, new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private Boolean checkString(String param, String splitter) {
        if (splitter.equals(".")) {
            if (param.split("").length < 22) {
                try {
                    Double.parseDouble(param);
                    String[] paramSplit = param.split("");
                    for (int i = 0; i < paramSplit.length; i++) {
                        if (paramSplit[i].equals(".")) {
                            return i <= 10 &&
                                    paramSplit.length - i <= 11 &&
                                    i != paramSplit.length - 1 &&
                                    i != 0;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }
        String[] paramSplit = param.split(splitter);
        if (paramSplit.length == 2 &&
                paramSplit[0].split("").length <= 10 &&
                paramSplit[1].split("").length <= 10) {
            try {
                Integer.parseInt(paramSplit[0]);
                Integer.parseInt(paramSplit[1]);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            if (paramSplit.length < 2) {
                try {
                    Integer.parseInt(param);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}

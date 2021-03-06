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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/request")
public class WebController {

    private static final String REGULAR_EXPRESSION = "^[0-9]{1,10}[.,][0-9]{1,10}$";

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

            if (checkParam(param)) {
                numParam.add(parameter);
            } else {
                strParam.add(parameter);
            }
        }

        Parameters parameters = new Parameters();
        parameters.setMethod(request.getMethod());
        parameters.setNumericParameters(numParam);
        parameters.setStringParameters(strParam);

        requestDetail.setClientInfo(clientInfo);
        requestDetail.setParameters(parameters);

        try {
            File tempFile = File.createTempFile("rstkm", null);

            convertObjectToXml(requestDetail, tempFile);

            BufferedReader br = new BufferedReader(new FileReader(tempFile));

            char[] arrayStrings = new char[(int) tempFile.length()];

            br.read(arrayStrings);

            tempFile.deleteOnExit();

            return String.valueOf(arrayStrings);
        } catch (Exception e) {
           return e.getMessage();
        }
    }

    private static void convertObjectToXml(RequestDetail requestDetail, File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(RequestDetail.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(requestDetail, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private Boolean checkParam(String param) {
        Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }
}

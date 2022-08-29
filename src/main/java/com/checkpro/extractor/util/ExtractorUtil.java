package com.checkpro.extractor.util;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtractorUtil {

    @Autowired
    private AWSSimpleSystemsManagement ssmClient;

    public String getParameterValue(String name) {
        GetParameterRequest request = new GetParameterRequest().withName(name);
        GetParameterResult response = ssmClient.getParameter(request);
        return response.getParameter().getValue();
    }
}

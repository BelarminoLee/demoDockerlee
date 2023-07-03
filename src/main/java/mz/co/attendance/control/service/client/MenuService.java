package mz.co.attendance.control.service.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mz.co.attendance.control.dao.entities.ussd.Menu;
import mz.co.attendance.control.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class MenuService {

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    /**
     * @return
     * @throws IOException
     */
    public Map<String, Menu> loadMenus(Language language) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String file = "ussd-menu-pt.json";
        if (language == Language.EN) {
            file = "ussd-menu-en.json";
        }
        Resource resource = resourceLoader.getResource("classpath:" + file);
        InputStream input = resource.getInputStream();
        String json = readFromInputStream(input);
        return objectMapper.readValue(json, new TypeReference<Map<String, Menu>>() {
        });
    }

}

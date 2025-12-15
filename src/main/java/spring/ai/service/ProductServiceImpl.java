package spring.ai.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductServiceImpl implements ProductService {

    private final VectorStore vectorStore;

    public ProductServiceImpl(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void loadProducts() {
        Resource resource = new ClassPathResource("products.csv");
        List<Document> documents = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\"[^\"]*\")|([^,]+)");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                List<String> values = new ArrayList<>();
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String value = matcher.group(0).replace("\"", "").trim();
                    values.add(value);
                }

                if (values.size() == 6) {
                    String name = values.get(0);
                    String category = values.get(1);
                    String tags = values.get(2);
                    String price = values.get(3);
                    String description = values.get(4);
                    String imageUrl = values.get(5);

                    // Create content for embedding
                    String content = "상품명: " + name + ", 카테고리: " + category + ", 태그: " + tags + ", 상세설명: " + description;

                    // Create metadata
                    Map<String, Object> metadata = new HashMap<>();
                    metadata.put("name", name);
                    metadata.put("category", category);
                    metadata.put("tags", tags);
                    metadata.put("price", Long.parseLong(price));
                    metadata.put("description", description);
                    metadata.put("image_url", imageUrl);
                    // product_id is not in the CSV, the vector store will generate a UUID.

                    documents.add(new Document(content, metadata));
                }
            }

            vectorStore.add(documents);

        } catch (IOException e) {
            // In a real application, you'd want to handle this exception more gracefully
            throw new RuntimeException("Failed to load products from CSV", e);
        }
    }
}

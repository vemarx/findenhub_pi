package com.example.finden_pi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String IBGE_API_BASE = "https://servicodados.ibge.gov.br/api/v1/localidades";

    /**
     * Busca todos os estados do Brasil
     * 
     * @return Lista de estados (sigla e nome)
     */
    public List<Map<String, String>> getAllStates() {
        try {
            String url = IBGE_API_BASE + "/estados?orderBy=nome";
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);
            List<Map<String, String>> states = new ArrayList<>();

            for (JsonNode node : jsonNode) {
                Map<String, String> state = new HashMap<>();
                state.put("sigla", node.get("sigla").asText());
                state.put("nome", node.get("nome").asText());
                states.add(state);
            }

            return states;
        } catch (Exception e) {
            // Retorna lista básica em caso de erro
            return getDefaultStates();
        }
    }

    /**
     * Busca todas as cidades de um estado
     * 
     * @param uf Sigla do estado (ex: SP, RJ, MG)
     * @return Lista de cidades do estado
     */
    public List<String> getCitiesByState(String uf) {
        try {
            String url = IBGE_API_BASE + "/estados/" + uf + "/municipios?orderBy=nome";
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);
            List<String> cities = new ArrayList<>();

            for (JsonNode node : jsonNode) {
                cities.add(node.get("nome").asText());
            }

            return cities;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Lista de estados padrão (fallback se API falhar)
     */
    private List<Map<String, String>> getDefaultStates() {
        List<Map<String, String>> states = new ArrayList<>();

        String[][] statesArray = {
                { "AC", "Acre" }, { "AL", "Alagoas" }, { "AP", "Amapá" },
                { "AM", "Amazonas" }, { "BA", "Bahia" }, { "CE", "Ceará" },
                { "DF", "Distrito Federal" }, { "ES", "Espírito Santo" },
                { "GO", "Goiás" }, { "MA", "Maranhão" }, { "MT", "Mato Grosso" },
                { "MS", "Mato Grosso do Sul" }, { "MG", "Minas Gerais" },
                { "PA", "Pará" }, { "PB", "Paraíba" }, { "PR", "Paraná" },
                { "PE", "Pernambuco" }, { "PI", "Piauí" }, { "RJ", "Rio de Janeiro" },
                { "RN", "Rio Grande do Norte" }, { "RS", "Rio Grande do Sul" },
                { "RO", "Rondônia" }, { "RR", "Roraima" }, { "SC", "Santa Catarina" },
                { "SP", "São Paulo" }, { "SE", "Sergipe" }, { "TO", "Tocantins" }
        };

        for (String[] state : statesArray) {
            Map<String, String> stateMap = new HashMap<>();
            stateMap.put("sigla", state[0]);
            stateMap.put("nome", state[1]);
            states.add(stateMap);
        }

        return states;
    }
}
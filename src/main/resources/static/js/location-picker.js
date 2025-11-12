/**
 * Location Picker - Busca estados e cidades da API do IBGE
 *
 * Como usar:
 * 1. Adicione este script no HTML: <script src="/js/location-picker.js"></script>
 * 2. Adicione os IDs nos selects: id="stateSelect" e id="citySelect"
 * 3. Chame: initLocationPicker()
 */

// Carrega estados automaticamente quando a página carrega
document.addEventListener("DOMContentLoaded", function () {
  const stateSelect = document.getElementById("stateSelect");
  const citySelect = document.getElementById("citySelect");

  if (stateSelect && citySelect) {
    initLocationPicker();
  }
});

/**
 * Inicializa o seletor de localização
 */
function initLocationPicker() {
  loadStates();
  setupStateListener();
}

/**
 * Carrega todos os estados
 */
async function loadStates() {
  const stateSelect = document.getElementById("stateSelect");

  if (!stateSelect) return;

  try {
    // Mostra loading
    stateSelect.innerHTML = '<option value="">Carregando estados...</option>';

    // Busca estados da API
    const response = await fetch("/api/locations/states");
    const states = await response.json();

    // Limpa e adiciona opção padrão
    stateSelect.innerHTML = '<option value="">Selecione o estado</option>';

    // Adiciona os estados
    states.forEach((state) => {
      const option = document.createElement("option");
      option.value = state.sigla;
      option.textContent = state.nome;
      stateSelect.appendChild(option);
    });

    // Se já tem um estado selecionado (edição), carrega as cidades
    if (stateSelect.value) {
      loadCities(stateSelect.value);
    }
  } catch (error) {
    console.error("Erro ao carregar estados:", error);
    stateSelect.innerHTML =
      '<option value="">Erro ao carregar estados</option>';
  }
}

/**
 * Carrega cidades de um estado
 */
async function loadCities(uf) {
  const citySelect = document.getElementById("citySelect");

  if (!citySelect || !uf) return;

  try {
    // Mostra loading
    citySelect.innerHTML = '<option value="">Carregando cidades...</option>';
    citySelect.disabled = true;

    // Busca cidades da API
    const response = await fetch(`/api/locations/cities/${uf}`);
    const cities = await response.json();

    // Limpa e adiciona opção padrão
    citySelect.innerHTML = '<option value="">Selecione a cidade</option>';

    // Adiciona as cidades
    cities.forEach((city) => {
      const option = document.createElement("option");
      option.value = city;
      option.textContent = city;
      citySelect.appendChild(option);
    });

    citySelect.disabled = false;
  } catch (error) {
    console.error("Erro ao carregar cidades:", error);
    citySelect.innerHTML = '<option value="">Erro ao carregar cidades</option>';
    citySelect.disabled = false;
  }
}

/**
 * Configura o listener do select de estados
 */
function setupStateListener() {
  const stateSelect = document.getElementById("stateSelect");

  if (!stateSelect) return;

  stateSelect.addEventListener("change", function () {
    const citySelect = document.getElementById("citySelect");

    if (this.value) {
      loadCities(this.value);
    } else {
      // Limpa o select de cidades se nenhum estado foi selecionado
      if (citySelect) {
        citySelect.innerHTML =
          '<option value="">Primeiro selecione um estado</option>';
        citySelect.disabled = true;
      }
    }
  });
}
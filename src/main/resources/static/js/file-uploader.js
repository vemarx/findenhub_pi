/**
 * File Uploader Universal - FindenPI
 * Sistema de upload de imagens e vídeos com preview
 *
 * Como usar:
 * 1. Adicione: <script src="/js/file-uploader.js"></script>
 * 2. Adicione atributos nos inputs:
 *    - data-upload-type="image" ou "video"
 *    - data-upload-folder="profiles" ou "services"
 *    - data-preview-id="idDoElementoDePreview"
 */

document.addEventListener("DOMContentLoaded", function () {
  initializeFileUploaders();
});

/**
 * Inicializa todos os uploaders da página
 */
function initializeFileUploaders() {
  const uploaders = document.querySelectorAll("[data-upload-type]");

  uploaders.forEach((input) => {
    input.addEventListener("change", handleFileSelect);
  });
}

/**
 * Manipula seleção de arquivo
 */
async function handleFileSelect(event) {
  const input = event.target;
  const file = input.files[0];

  if (!file) return;

  const uploadType = input.dataset.uploadType || "image";
  const folder = input.dataset.uploadFolder || "general";
  const previewId = input.dataset.previewId;
  const urlInputId = input.dataset.urlInput;

  // Validações básicas
  if (uploadType === "image") {
    if (!file.type.startsWith("image/")) {
      showUploadError("Por favor, selecione uma imagem válida");
      input.value = "";
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      showUploadError("Imagem muito grande. Máximo 10MB");
      input.value = "";
      return;
    }
  } else if (uploadType === "video") {
    if (!file.type.startsWith("video/")) {
      showUploadError("Por favor, selecione um vídeo válido");
      input.value = "";
      return;
    }

    if (file.size > 100 * 1024 * 1024) {
      showUploadError("Vídeo muito grande. Máximo 100MB");
      input.value = "";
      return;
    }
  }

  // Mostrar preview local
  if (previewId) {
    showLocalPreview(file, previewId, uploadType);
  }

  // Fazer upload
  await uploadFile(file, uploadType, folder, previewId, urlInputId);
}

/**
 * Mostra preview local do arquivo
 */
function showLocalPreview(file, previewId, type) {
  const preview = document.getElementById(previewId);
  if (!preview) return;

  const reader = new FileReader();

  reader.onload = function (e) {
    if (type === "image") {
      preview.src = e.target.result;
      preview.style.display = "block";
    } else if (type === "video") {
      preview.src = e.target.result;
      preview.style.display = "block";
    }
  };

  reader.readAsDataURL(file);
}

/**
 * Faz upload do arquivo para o servidor
 */
async function uploadFile(file, type, folder, previewId, urlInputId) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("folder", folder);

  const endpoint = type === "video" ? "/api/upload/video" : "/api/upload/image";

  // Mostrar loading
  showUploadLoading(true);

  try {
    const response = await fetch(endpoint, {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (response.ok) {
      // Sucesso!
      const url = data.url;

      // Atualizar input hidden com URL
      if (urlInputId) {
        const urlInput = document.getElementById(urlInputId);
        if (urlInput) {
          urlInput.value = url;
        }
      }

      // Atualizar preview com imagem do servidor
      if (previewId) {
        const preview = document.getElementById(previewId);
        if (preview) {
          preview.src = url;
        }
      }

      showUploadSuccess("Upload realizado com sucesso!");
    } else {
      showUploadError(data.error || "Erro ao fazer upload");
    }
  } catch (error) {
    console.error("Erro no upload:", error);
    showUploadError("Erro ao conectar com o servidor");
  } finally {
    showUploadLoading(false);
  }
}

/**
 * Mostra loading durante upload
 */
function showUploadLoading(show) {
  let loader = document.getElementById("upload-loader");

  if (!loader) {
    loader = document.createElement("div");
    loader.id = "upload-loader";
    loader.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(0,0,0,0.8);
            color: white;
            padding: 20px 40px;
            border-radius: 10px;
            z-index: 9999;
            display: none;
        `;
    loader.innerHTML = `
            <div class="spinner-border text-light" role="status">
                <span class="visually-hidden">Enviando...</span>
            </div>
            <p class="mt-2 mb-0">Fazendo upload...</p>
        `;
    document.body.appendChild(loader);
  }

  loader.style.display = show ? "block" : "none";
}

/**
 * Mostra mensagem de sucesso
 */
function showUploadSuccess(message) {
  showToast(message, "success");
}

/**
 * Mostra mensagem de erro
 */
function showUploadError(message) {
  showToast(message, "danger");
}

/**
 * Mostra toast notification
 */
function showToast(message, type) {
  let toast = document.getElementById("upload-toast");

  if (!toast) {
    toast = document.createElement("div");
    toast.id = "upload-toast";
    toast.className = "toast-container position-fixed bottom-0 end-0 p-3";
    toast.style.zIndex = "10000";
    document.body.appendChild(toast);
  }

  const toastEl = document.createElement("div");
  toastEl.className = `toast align-items-center text-white bg-${type} border-0`;
  toastEl.setAttribute("role", "alert");
  toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${type === "success" ? "✅" : "❌"} ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;

  toast.appendChild(toastEl);

  const bsToast = new bootstrap.Toast(toastEl, { delay: 3000 });
  bsToast.show();

  toastEl.addEventListener("hidden.bs.toast", function () {
    toastEl.remove();
  });
}

/**
 * Adiciona múltiplas imagens (para galeria de serviços)
 */
async function uploadMultipleImages(files, folder, containerId) {
  const container = document.getElementById(containerId);
  if (!container) return;

  for (let file of files) {
    if (!file.type.startsWith("image/")) continue;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("folder", folder);

    try {
      const response = await fetch("/api/upload/image", {
        method: "POST",
        body: formData,
      });

      const data = await response.json();

      if (response.ok) {
        addImageToGallery(data.url, container);
      }
    } catch (error) {
      console.error("Erro no upload:", error);
    }
  }
}

/**
 * Adiciona imagem à galeria
 */
function addImageToGallery(url, container) {
  const div = document.createElement("div");
  div.className = "col-md-3 mb-3";
  div.innerHTML = `
        <div class="position-relative">
            <img src="${url}" class="img-fluid rounded" style="object-fit: cover; height: 150px; width: 100%;">
            <button type="button" class="btn btn-danger btn-sm position-absolute top-0 end-0 m-2" 
                    onclick="this.closest('.col-md-3').remove()">
                <i class="bi bi-trash"></i>
            </button>
            <input type="hidden" name="serviceImages[]" value="${url}">
        </div>
    `;
  container.appendChild(div);
}

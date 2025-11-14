/**
 * Gerenciador de Portfólio - Upload, edição e organização de fotos/vídeos
 */

// Upload de imagens para o portfólio
async function uploadPortfolioImage(file) {
  const formData = new FormData();
  formData.append("file", file);

  showUploadLoading(true);

  try {
    const response = await fetch("/api/portfolio/upload-image", {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (response.ok && data.success) {
      addPortfolioItemToGrid(data.item);
      showUploadSuccess("Imagem adicionada ao portfólio!");
      return true;
    } else {
      showUploadError(data.error || "Erro ao adicionar imagem");
      return false;
    }
  } catch (error) {
    showUploadError("Erro ao conectar com o servidor");
    return false;
  } finally {
    showUploadLoading(false);
  }
}

// Upload de vídeos para o portfólio
async function uploadPortfolioVideo(file) {
  const formData = new FormData();
  formData.append("file", file);

  showUploadLoading(true);

  try {
    const response = await fetch("/api/portfolio/upload-video", {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (response.ok && data.success) {
      addPortfolioItemToGrid(data.item);
      showUploadSuccess("Vídeo adicionado ao portfólio!");
      return true;
    } else {
      showUploadError(data.error || "Erro ao adicionar vídeo");
      return false;
    }
  } catch (error) {
    showUploadError("Erro ao conectar com o servidor");
    return false;
  } finally {
    showUploadLoading(false);
  }
}

// Adiciona item ao grid de portfólio
function addPortfolioItemToGrid(item) {
  const grid = document.getElementById("portfolioGrid");
  if (!grid) return;

  const col = document.createElement("div");
  col.className = "col-md-4 col-lg-3 portfolio-item";
  col.dataset.itemId = item.id;

  const isVideo = item.mediaType === "VIDEO";

  col.innerHTML = `
    <div class="card portfolio-card h-100">
      <div class="position-relative">
        ${
          isVideo
            ? `
          <video src="${item.url}" class="card-img-top portfolio-media" style="height: 200px; object-fit: cover;"></video>
          <div class="video-overlay">
            <i class="bi bi-play-circle-fill text-white" style="font-size: 3rem;"></i>
          </div>
        `
            : `
          <img src="${item.url}" class="card-img-top portfolio-media" alt="Portfolio" style="height: 200px; object-fit: cover;">
        `
        }
        <div class="portfolio-actions">
          <button class="btn btn-sm btn-light" onclick="editPortfolioDescription('${
            item.id
          }')" title="Editar descrição">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-sm btn-danger" onclick="deletePortfolioItem('${
            item.id
          }')" title="Deletar">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      </div>
      <div class="card-body p-2">
        <p class="card-text small text-muted mb-0 portfolio-description" id="desc-${
          item.id
        }">
          ${item.description || "Sem descrição"}
        </p>
        <small class="text-muted">
          <i class="bi bi-${isVideo ? "camera-video" : "image"}"></i>
          ${isVideo ? "Vídeo" : "Imagem"}
        </small>
      </div>
    </div>
  `;

  grid.appendChild(col);
  updatePortfolioCount();
}

// Editar descrição de um item
async function editPortfolioDescription(itemId) {
  const descElement = document.getElementById(`desc-${itemId}`);
  const currentDesc = descElement.textContent.trim();
  const newDesc = prompt(
    "Digite a nova descrição:",
    currentDesc === "Sem descrição" ? "" : currentDesc
  );

  if (newDesc === null) return; // Cancelou

  try {
    const response = await fetch(`/api/portfolio/${itemId}/description`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ description: newDesc }),
    });

    const data = await response.json();

    if (response.ok && data.success) {
      descElement.textContent = newDesc || "Sem descrição";
      showUploadSuccess("Descrição atualizada!");
    } else {
      showUploadError(data.error || "Erro ao atualizar descrição");
    }
  } catch (error) {
    showUploadError("Erro ao conectar com o servidor");
  }
}

// Deletar item do portfólio
async function deletePortfolioItem(itemId) {
  if (!confirm("Tem certeza que deseja remover este item do portfólio?")) {
    return;
  }

  try {
    const response = await fetch(`/api/portfolio/${itemId}`, {
      method: "DELETE",
    });

    const data = await response.json();

    if (response.ok && data.success) {
      const itemElement = document.querySelector(`[data-item-id="${itemId}"]`);
      if (itemElement) {
        itemElement.remove();
      }
      updatePortfolioCount();
      showUploadSuccess("Item removido com sucesso!");
    } else {
      showUploadError(data.error || "Erro ao remover item");
    }
  } catch (error) {
    showUploadError("Erro ao conectar com o servidor");
  }
}

// Atualiza contador de itens
function updatePortfolioCount() {
  const grid = document.getElementById("portfolioGrid");
  const countElement = document.getElementById("portfolioCount");

  if (grid && countElement) {
    const count = grid.children.length;
    countElement.textContent = count;
  }
}

// Handler para input de imagens
document.addEventListener("DOMContentLoaded", function () {
  const imageInput = document.getElementById("portfolioImageInput");
  if (imageInput) {
    imageInput.addEventListener("change", async function (e) {
      const files = Array.from(e.target.files);

      for (const file of files) {
        if (!file.type.startsWith("image/")) {
          showUploadError("Por favor, selecione apenas imagens");
          continue;
        }
        if (file.size > 10 * 1024 * 1024) {
          showUploadError(`Imagem ${file.name} muito grande. Máximo 10MB`);
          continue;
        }

        await uploadPortfolioImage(file);
      }

      // Limpa o input para permitir upload do mesmo arquivo novamente
      e.target.value = "";
    });
  }

  // Handler para input de vídeos
  const videoInput = document.getElementById("portfolioVideoInput");
  if (videoInput) {
    videoInput.addEventListener("change", async function (e) {
      const files = Array.from(e.target.files);

      for (const file of files) {
        if (!file.type.startsWith("video/")) {
          showUploadError("Por favor, selecione apenas vídeos");
          continue;
        }
        if (file.size > 100 * 1024 * 1024) {
          showUploadError(`Vídeo ${file.name} muito grande. Máximo 100MB`);
          continue;
        }

        await uploadPortfolioVideo(file);
      }

      // Limpa o input
      e.target.value = "";
    });
  }
});

// Visualizar mídia em modal
function viewPortfolioMedia(url, type) {
  const modal = document.getElementById("mediaViewModal");
  const modalBody = modal.querySelector(".modal-body");

  if (type === "VIDEO") {
    modalBody.innerHTML = `<video src="${url}" controls class="w-100"></video>`;
  } else {
    modalBody.innerHTML = `<img src="${url}" class="img-fluid w-100" alt="Portfolio">`;
  }

  const bsModal = new bootstrap.Modal(modal);
  bsModal.show();
}

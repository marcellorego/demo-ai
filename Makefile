SHELL:=/bin/bash
.DEFAULT_GOAL := help
.PHONY: help user home ps prune envUp envDown

CURRENT_DIR:=$(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
USER_ID:=$(shell id -u ${USER})
USER_GROUP:=$(shell id -g ${USER})
APPLICATION_NAME=demo-ollama
CONTAINER_NAME?=$(APPLICATION_NAME)_$(shell openssl rand -hex 8)
GRADLE_DIR:=$(shell echo $(HOME)/.gradle)
SUDO:=$(if $(filter Windows_NT, $(OS)),,sudo)
DEFAULT_GPUS?=all
DEFAULT_MODEL?=llama3.1

help:
	@echo ''
	@echo 'Makefile for '
	@echo '     make help			show this information'
	@echo '     make ps			docker ps -a'
	@echo '     make prune			run docker prune'
	@echo '     make ollamaCPU		runs ollama container with CPU'
	@echo '     make ollamaGPU		runs ollama container with GPU'
	@echo '     make ollamaModel	runs provided model inside ollama container'
	@echo '     make ollamaUp		start ollama compose'
	@echo '     make ollamaDown		stop ollama compose'

ps:
	docker ps -a

prune:
	docker system prune -f && \
		docker volume prune -f && \
		docker network prune -f

NvidiaGPU:
    # Install the NVIDIA Container Toolkit via APT
    # Configure the repository
	curl -fsSL https://nvidia.github.io/libnvidia-container/gpgkey \
	    | sudo gpg --dearmor -o /usr/share/keyrings/nvidia-container-toolkit-keyring.gpg
	curl -s -L https://nvidia.github.io/libnvidia-container/stable/deb/nvidia-container-toolkit.list \
	    | sed 's#deb https://#deb [signed-by=/usr/share/keyrings/nvidia-container-toolkit-keyring.gpg] https://#g' \
	    | sudo tee /etc/apt/sources.list.d/nvidia-container-toolkit.list
	sudo apt-get update
	# Install the NVIDIA Container Toolkit packages
	sudo apt-get install -y nvidia-container-toolkit
	# Configure Docker to use Nvidia driver
	sudo nvidia-ctk runtime configure --runtime=docker
	# Restart Docker
	sudo systemctl restart docker

ollamaCPU:
	docker run -it --rm \
        --name ollama \
        -p 11434:11434 \
        -v $(CURRENT_DIR)/docker-compose/ollama:/root/.ollama \
         ollama/ollama:latest

ollamaGPU:
	docker run -it --rm \
        --name ollama \
        -p 11434:11434 \
        -v $(CURRENT_DIR)/docker-compose/ollama:/root/.ollama \
        --gpus $(DEFAULT_GPUS) \
        ollama/ollama:latest

ollamaModel:
	docker exec -it ollama ollama run $(DEFAULT_MODEL)

ollamaUp:
	docker-compose -f docker-compose/docker-compose-env.yaml up -d

ollamaDown:
	docker-compose -f docker-compose/docker-compose-env.yaml down

localAI:
	docker run -it --rm \
        --name local-ai \
        -p 15777:8080 \
        -v $(CURRENT_DIR)/docker-compose/localai:/build/models:cached \
        localai/localai:latest-aio-cpu
# Alternative images:
# --gpus $(DEFAULT_GPUS) \
# - if you have an Nvidia GPU:
# docker run -ti --name local-ai -p 8080:8080 --gpus all localai/localai:latest-aio-gpu-nvidia-cuda-12
# - without preconfigured models
# docker run -ti --name local-ai -p 8080:8080 localai/localai:latest
# - without preconfigured models for Nvidia GPUs
# docker run -ti --name local-ai -p 8080:8080 --gpus all localai/localai:latest-gpu-nvidia-cuda-12


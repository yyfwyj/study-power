<!-- 命令式API（函数调用）/声明式API（JSX/Template） -->
<template>
    <Teleport to="body">
        <Transition name="modal">
            <div v-if="visible" class="modal-overlay" @click.self="handleClose">
                <div class="modal-container">
                    <div class="modal-header">
                        <h2 class="modal-title">{{ title }}</h2>
                        <button class="modal-close" @click="handleClose">×</button>
                    </div>
                    <div class="modal-body">
                        <slot></slot>
                    </div>
                    <div class="modal-footer" v-if="showFooter">
                        <button class="btn btn-cancel" @click="handleCancel">取消</button>
                        <button class="btn btn-confirm" @click="handleConfirm">确认</button>
                    </div>
                </div>
            </div>
        </Transition>
    </Teleport>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
    title: {
        type: String,
        default: "标题"
    },
    visible: {
        type: Boolean,
        default: false
    },
    showFooter: {
        type: Boolean,
        default: true
    },
    closeOnClickOverlay: {
        type: Boolean,
        default: true
    },
    closeOnPressEscape: {
        type: Boolean,
        default: true
    }
})

const emit = defineEmits(['update:visible', 'cancel', 'confirm']);

const handleClose = () => {
    if (props.closeOnClickOverlay) {
        emit('update:visible', false);
        emit('close');
    }
}

const handleConfirm = () => {
    emit('confirm')
    emit('update:visible', false)
    emit('close')
}

const handleCancel = () => {
    emit('cancel')
    emit('update:visible', false)
    emit('close')
}

// 监听ESC
const handleKeydown = (e) => {
    if (e.key === 'Escape' && props.visible && props.closeOnPressEscape) {
        handleCancel();
    }
}

// watch
watch(() => props.visible, (newVal) => {
    if (newVal) {
        document.addEventListener('keydown', handleKeydown);
        // 禁止背景滚动
        document.body.style.overflow = 'hidden';
    } else {
        document.removeEventListener('keydown', handleKeydown);
        document.body.style.overflow = 'auto';
    }
})
</script>

<style>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    backdrop-filter: blur(4px);
}

.modal-container {
    background: white;
    border-radius: 12px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    max-width: 500px;
    width: 90%;
    max-height: 80vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

.modal-header {
    padding: 20px 24px;
    border-bottom: 1px solid #eee;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.modal-title {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
}

.modal-close {
    background: none;
    border: none;
    font-size: 28px;
    color: #999;
    cursor: pointer;
    padding: 0;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 4px;
    transition: all 0.2s;
}

.modal-close:hover {
  background: #f5f5f5;
  color: #333;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  flex: 1;
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.btn {
  padding: 8px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-cancel {
  background: #f5f5f5;
  color: #333;
}

.btn-cancel:hover {
  background: #e8e8e8;
}

.btn-confirm {
  background: #667eea;
  color: white;
}

.btn-confirm:hover {
  background: #5568d3;
}

.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-active .modal-container,
.modal-leave-active .modal-container {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  transform: scale(0.9) translateY(-20px);
  opacity: 0;
}
</style>
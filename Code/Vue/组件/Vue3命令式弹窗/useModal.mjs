import { createApp, h, ref } from "vue";
import Modal from "./Modal.vue";

// 创建弹窗示例的函数
export function createModal(options = {}) {
  // 创建容器元素
  const container = document.createElement("div");
  document.body.appendChild(container);

  // 创建Vue应用实例
  const app = createApp({
    setup() {
      const visible = ref(true);

      const close = () => {
        visible.value = false;
        // 延迟销毁，等待动画完成
        setTimeout(() => {
          app.unmount();
          document.body.removeChild(container);
        }, 300);
      };

      const confirm = () => {
        if (options.onConfirm) {
          options.onConfirm();
        }
        close();
      };

      const cancel = () => {
        if (options.onCancel) {
          options.onCancel();
        }
        close();
      };

      return () =>
        h(
          Modal,
          {
            visible: visible.value,
            title: options.title || "提示",
            showFooter: options.showFooter !== false,
            "onUpdate:visible": (val) => {
              visible.value = val;
              if (!val) close();
            },
            onClose: close,
            onConfirm: confirm,
            onCancel: cancel,
          },
          {
            default: () => options.content || options.render?.(),
          }
        );
    },
  });

  app.mount(container);

  return {
    close: () => {
      const visible = app._instance?.setupState?.visible;
      if (visible) {
        visible.value = false;
      }
    },
  };
}

// 基础弹窗
export function showModal(options) {
  return createModal(options);
}

// 确认对话框
export function showConfirm(options) {
  return createModal({
    title: options.title || "确认",
    content: options.content || options.message || "确定要执行此操作吗？",
    showFooter: true,
    onConfirm: options.onConfirm,
    onCancel: options.onCancel,
  });
}

// 警告提示
export function showAlert(options) {
  return createModal({
    title: options.title || "提示",
    content: options.content || options.message || "操作成功",
    showFooter: false,
    closeOnClickOverlay: true,
  });
}

// 输入对话框（扩展功能）
export function showPrompt(options) {
  // 可以扩展为带输入框的弹窗
  // 这里先返回基础实现
  return createModal({
    title: options.title || "输入",
    content: options.content || "",
    showFooter: true,
    onConfirm: () => {
      // 获取输入值并回调
      if (options.onConfirm) {
        options.onConfirm(/* 输入值 */);
      }
    },
  });
}

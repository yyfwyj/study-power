<template>
  <section class="dynamic-slot">
    <div class="dynamic-slot__lane">
      <div class="dynamic-slot__label">Header</div>
      <slot name="header" />
    </div>
    <div class="dynamic-slot__lane">
      <div class="dynamic-slot__label">Footer</div>
      <slot name="footer" />
    </div>
  </section>
</template>

<style>
.dynamic-slot {
  border-radius: 14px;
  border: 1px solid #e7e0d9;
  background: #ffffff;
  padding: 14px;
  display: grid;
  gap: 12px;
}

.dynamic-slot__lane {
  border-radius: 12px;
  padding: 12px;
  background: #f7f4f0;
  display: grid;
  gap: 6px;
}

.dynamic-slot__label {
  font-size: 12px;
  font-weight: 600;
  color: #2c6b7c;
  text-transform: uppercase;
  letter-spacing: 0.8px;
}
</style>

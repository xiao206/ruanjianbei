<template>
  <div class="graph-container">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>能力图谱</span>
          <div class="header-actions">
            <el-select v-model="graphType" @change="loadGraph" placeholder="选择图谱类型">
              <el-option label="人才能力图谱" value="person" />
              <el-option label="需求图谱" value="requirement" />
            </el-select>
            <el-input v-model="entityId" placeholder="输入实体ID" style="width: 200px; margin-left: 10px" />
            <el-button type="primary" @click="loadGraph" :loading="loading" style="margin-left: 10px">加载图谱</el-button>
          </div>
        </div>
      </template>
      <div class="graph-content">
        <div class="graph-toolbar">
          <el-button size="small" @click="zoomIn">放大</el-button>
          <el-button size="small" @click="zoomOut">缩小</el-button>
          <el-button size="small" @click="resetLayout">重置布局</el-button>
          <el-button size="small" @click="switchLayout">切换布局</el-button>
        </div>
        <div ref="graphRef" class="graph-canvas" v-loading="loading">
          <el-empty v-if="!graph && !loading" description="请输入实体ID后点击加载图谱" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { Graph } from '@antv/g6'
import { ElMessage } from 'element-plus'

const graphRef = ref(null)
const graph = ref(null)
const graphType = ref('person')
const entityId = ref('person1')
const currentLayout = ref('dagre')
const loading = ref(false)

const loadGraph = async () => {
  const id = String(entityId.value || '').trim()
  if (!id) {
    ElMessage.warning('请输入实体ID')
    return
  }

  loading.value = true
  try {
    await new Promise(resolve => setTimeout(resolve, 600))
    await nextTick()
    await renderGraph(buildMockGraphData({ graphType: graphType.value, entityId: id }))
    ElMessage.success('图谱加载成功')
  } catch (error) {
    ElMessage.error('图谱加载失败')
  } finally {
    loading.value = false
  }
}

const buildMockGraphData = ({ graphType, entityId }) => {
  if (graphType === 'person') {
    return {
      nodes: [
        { id: entityId, label: 'Person', name: entityId },
        { id: 'skill-java', label: 'Skill', name: 'Java' },
        { id: 'skill-spring', label: 'Skill', name: 'Spring Boot' },
        { id: 'skill-mysql', label: 'Skill', name: 'MySQL' },
        { id: 'skill-vue', label: 'Skill', name: 'Vue 3' }
      ],
      edges: [
        { id: `${entityId}-java`, source: entityId, target: 'skill-java', label: 'HAS_SKILL' },
        { id: `${entityId}-spring`, source: entityId, target: 'skill-spring', label: 'HAS_SKILL' },
        { id: `${entityId}-mysql`, source: entityId, target: 'skill-mysql', label: 'HAS_SKILL' },
        { id: `${entityId}-vue`, source: entityId, target: 'skill-vue', label: 'HAS_SKILL' }
      ]
    }
  }

  return {
    nodes: [
      { id: entityId, label: 'Requirement', name: entityId },
      { id: 'skill-java', label: 'Skill', name: 'Java' },
      { id: 'skill-spring', label: 'Skill', name: 'Spring Boot' },
      { id: 'skill-docker', label: 'Skill', name: 'Docker' },
      { id: 'skill-k8s', label: 'Skill', name: 'Kubernetes' }
    ],
    edges: [
      { id: `${entityId}-java`, source: entityId, target: 'skill-java', label: 'REQUIRES' },
      { id: `${entityId}-spring`, source: entityId, target: 'skill-spring', label: 'REQUIRES' },
      { id: `${entityId}-docker`, source: entityId, target: 'skill-docker', label: 'REQUIRES' },
      { id: `${entityId}-k8s`, source: entityId, target: 'skill-k8s', label: 'REQUIRES' }
    ]
  }
}

const renderGraph = async (graphData) => {
  if (!graphRef.value) return

  const nodeCount = graphData?.nodes?.length ?? 0
  const gridLayout = nodeCount > 300
    ? { type: 'grid', sortBy: 'id' }
    : { type: 'grid' }

  if (!graph.value) {
    graph.value = new Graph({
      container: graphRef.value,
      autoResize: true,
      devicePixelRatio: 1,
      data: { nodes: [], edges: [] },
      behaviors: [
        { type: 'drag-canvas' },
        { type: 'zoom-canvas' },
        { type: 'drag-element', shadow: true, hideEdge: 'all' }
      ],
      node: {
        style: (d) => {
          const base = {
            size: 28,
            labelText: d.name || d.id,
            labelPlacement: 'bottom',
            labelOffsetY: 8,
            labelFill: '#333',
            labelFontSize: 12,
            lineWidth: 2,
            fill: '#C6E5FF',
            stroke: '#1890FF'
          }
          if (d.label === 'Person') return { ...base, fill: '#FFE58F', stroke: '#FAAD14' }
          if (d.label === 'Skill') return { ...base, fill: '#B7EB8F', stroke: '#52C41A' }
          if (d.label === 'Requirement') return { ...base, fill: '#FFD6E7', stroke: '#EB2F96' }
          return base
        }
      },
      edge: {
        style: (d) => ({
          stroke: d.label === 'HAS_SKILL' ? '#1890FF' : '#999',
          lineWidth: d.label === 'HAS_SKILL' ? 2 : 1,
          endArrow: true,
          labelText: d.label || '',
          labelFill: '#666',
          labelFontSize: 10
        })
      }
    })
  }

  graph.value.setLayout(currentLayout.value === 'grid' ? gridLayout : { type: 'dagre', rankdir: 'TB' })
  graph.value.setData(graphData)
  await graph.value.render()
}

const zoomIn = () => {
  if (graph.value) {
    void graph.value.zoomBy(1.2)
  }
}

const zoomOut = () => {
  if (graph.value) {
    void graph.value.zoomBy(0.8)
  }
}

const resetLayout = () => {
  if (graph.value) {
    void graph.value.fitView()
  }
}

const switchLayout = () => {
  currentLayout.value = currentLayout.value === 'dagre' ? 'grid' : 'dagre'
  loadGraph()
}

onMounted(() => {
  loadGraph()
})

onUnmounted(() => {
  if (graph.value) {
    graph.value.destroy()
    graph.value = null
  }
})
</script>

<style scoped>
.graph-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.graph-content {
  margin-top: 20px;
}

.graph-toolbar {
  margin-bottom: 10px;
}

.graph-toolbar .el-button {
  margin-right: 10px;
}

.graph-canvas {
  width: 100%;
  height: 600px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
}
</style>

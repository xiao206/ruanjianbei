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
            <el-button type="primary" @click="loadGraph" style="margin-left: 10px">加载图谱</el-button>
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
        <div ref="graphRef" class="graph-canvas"></div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import G6 from '@antv/g6'
import { ElMessage } from 'element-plus'

const graphRef = ref(null)
const graph = ref(null)
const graphType = ref('person')
const entityId = ref('person1')
const currentLayout = ref('force')

const loadGraph = async () => {
  try {
    let url = ''
    if (graphType.value === 'person') {
      url = `/api/v1/graph/person/${entityId.value}`
    } else {
      url = `/api/v1/graph/requirement/${entityId.value}`
    }
    
    const response = await fetch(url)
    const data = await response.json()
    
    if (data.code === 200) {
      renderGraph(data.data)
      ElMessage.success('图谱加载成功')
    } else {
      ElMessage.error('图谱加载失败: ' + data.message)
    }
  } catch (error) {
    ElMessage.error('网络错误: ' + error.message)
  }
}

const renderGraph = (graphData) => {
  if (graph.value) {
    graph.value.destroy()
  }
  
  const container = graphRef.value
  const width = container.clientWidth
  const height = container.clientHeight
  
  // 创建 G6 图实例
  graph.value = new G6.Graph({
    container: container,
    width: width,
    height: height,
    modes: {
      default: ['drag-canvas', 'zoom-canvas', 'drag-node']
    },
    layout: {
      type: currentLayout.value,
      force: {
        repulsion: 1000,
        edgeLength: 100
      },
      hierarchical: {
        direction: 'TB',
        sortByComboSize: true
      }
    },
    defaultNode: {
      size: 30,
      style: {
        fill: '#C6E5FF',
        stroke: '#1890FF',
        lineWidth: 2
      },
      labelCfg: {
        style: {
          fill: '#333',
          fontSize: 12
        }
      }
    },
    defaultEdge: {
      style: {
        stroke: '#999',
        lineWidth: 1,
        endArrow: true
      },
      labelCfg: {
        style: {
          fill: '#666',
          fontSize: 10
        }
      }
    }
  })
  
  // 处理节点样式
  graphData.nodes.forEach(node => {
    if (node.label === 'Person') {
      node.style = {
        fill: '#FFE58F',
        stroke: '#FAAD14',
        lineWidth: 2
      }
    } else if (node.label === 'Skill') {
      node.style = {
        fill: '#B7EB8F',
        stroke: '#52C41A',
        lineWidth: 2
      }
    }
  })
  
  // 处理边样式
  graphData.edges.forEach(edge => {
    if (edge.label === 'HAS_SKILL') {
      edge.style = {
        stroke: '#1890FF',
        lineWidth: 2,
        endArrow: true
      }
    }
  })
  
  // 加载数据并渲染
  graph.value.data(graphData)
  graph.value.render()
}

const zoomIn = () => {
  if (graph.value) {
    graph.value.zoom(1.2)
  }
}

const zoomOut = () => {
  if (graph.value) {
    graph.value.zoom(0.8)
  }
}

const resetLayout = () => {
  if (graph.value) {
    graph.value.fitView()
  }
}

const switchLayout = () => {
  if (currentLayout.value === 'force') {
    currentLayout.value = 'hierarchical'
  } else {
    currentLayout.value = 'force'
  }
  loadGraph()
}

onMounted(() => {
  loadGraph()
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
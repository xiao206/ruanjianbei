import express from 'express';
import cors from 'cors';
import bodyParser from 'body-parser';

const app = express();
app.use(cors());
app.use(bodyParser.json());

const router = express.Router();

// Auth
router.post('/auth/login', (req, res) => {
  const { username } = req.body;
  if (username === 'admin') {
    res.json({ code: 200, message: 'Success', data: { token: 'mock-token-admin', user: { id: 1, username: 'admin', userType: 'admin' } } });
  } else {
    res.json({ code: 200, message: 'Success', data: { token: 'mock-token-user', user: { id: 2, username: 'user1', userType: 'user' } } });
  }
});

// Admin Users
router.get('/admin/users', (req, res) => {
  res.json({
    code: 200,
    data: [
      { id: 1, username: 'admin', userType: 'admin', status: 'active', createTime: '2026-04-27' },
      { id: 2, username: 'user1', userType: 'user', status: 'active', createTime: '2026-04-27' }
    ]
  });
});

// Admin Skills
router.get('/admin/skills', (req, res) => {
  res.json({
    code: 200,
    data: [
      { id: 1, name: 'Java', category: 'Backend', level: 'Expert' },
      { id: 2, name: 'Vue', category: 'Frontend', level: 'Advanced' }
    ]
  });
});

// Admin Monitor
router.get('/admin/monitor', (req, res) => {
  res.json({
    code: 200,
    data: {
      services: [
        { name: 'gateway', status: 'up' },
        { name: 'admin-service', status: 'up' },
        { name: 'doc-service', status: 'up' }
      ],
      databases: [
        { name: 'MySQL', status: 'up' },
        { name: 'Neo4j', status: 'up' }
      ],
      aiModels: [
        { name: 'Ollama', status: 'up' }
      ]
    }
  });
});

// Document Upload (Mock)
router.post('/document/upload', (req, res) => {
  res.json({ code: 200, data: { id: 101, name: 'Resume.pdf' } });
});

router.get('/document/:id/status', (req, res) => {
  res.json({ code: 200, data: { status: 'completed' } });
});

router.get('/document/:id/result', (req, res) => {
  res.json({
    code: 200,
    data: {
      skills: [{ name: 'Java', level: '熟练' }, { name: 'Vue', level: '掌握' }],
      projects: ['AI智能匹配系统', '电商后台']
    }
  });
});

// Graph
router.get('/graph/query', (req, res) => {
  res.json({
    code: 200,
    data: {
      nodes: [
        { id: '1', label: 'Person', name: 'user1' },
        { id: '2', label: 'Skill', name: 'Java' },
        { id: '3', label: 'Skill', name: 'Vue' }
      ],
      edges: [
        { source: '1', target: '2', label: 'HAS_SKILL' },
        { source: '1', target: '3', label: 'HAS_SKILL' }
      ]
    }
  });
});

// Match
router.post('/match/execute', (req, res) => {
  res.json({
    code: 200,
    data: [
      {
        personId: '1',
        personName: 'user1',
        score: 95,
        matchSkills: ['Java', 'Vue'],
        gapSkills: ['Python'],
        learningPath: '建议学习Python基础'
      },
      {
        personId: '2',
        personName: 'user2',
        score: 80,
        matchSkills: ['Java'],
        gapSkills: ['Vue', 'Python'],
        learningPath: '建议学习前端Vue框架'
      }
    ]
  });
});

app.use('/v1', router);

app.listen(8080, () => {
  console.log('Mock server listening on port 8080');
});

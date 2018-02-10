import index from '@/pages/index'
import contact from '@/pages/contact'
import login from '@/pages/login'
import questionBank from '@/pages/questionBank'
import createBank from '@/pages/createBank'
import questionList from '@/pages/questionList'
import setting from '@/pages/setting'

const router = [
  {
    path: '/',
    name: 'index',
    component: index
  },
  {
    path: '/contact',
    name: 'contact',
    component: contact
  },
  {
    path: '/login',
    name: 'login',
    component: login
  },
  {
    path: '/questionBank',
    name: 'questionBank',
    component: questionBank
  },{
    path: '/questionBank/createBank',
    name: 'createBank',
    component: createBank
  },{
    path: '/questionBank/quest/list',
    name: 'questionList',
    component: questionList
  },{
    path: '/setting',
    name: 'setting',
    component: setting
  }
]

export default router

const Register = r => require.ensure([], () => r(require('@/pages/user/register')), 'Register') // 注册
const Login = r => require.ensure([], () => r(require('@/pages/user/login')), 'Login') // 登录
const AgentProtocol = r => require.ensure([], () => r(require('@/pages/common/agentProtocol')), 'AgentProtocol') // 代理协议
const AboutUs = r => require.ensure([], () => r(require('@/pages/common/aboutUs')), 'AboutUs') // 关于我们
const GoodsList = r => require.ensure([], () => r(require('@/pages/goods/goodsList')), 'GoodsList') // 商品
const GoodsDetail = r => require.ensure([], () => r(require('@/pages/goods/goodsDetail')), 'GoodsDetail') // 商品详情
const Mine = r => require.ensure([], () => r(require('@/pages/mine/mine')), 'Mine') // 我的
const PersonalOrder = r => require.ensure([], () => r(require('@/pages/mine/order/personalOrder')), 'PersonalOrder') // 个人订单
const OrderDetail = r => require.ensure([], () => r(require('@/pages/mine/order/orderDetail')), 'OrderDetail') // 订单详情
const PayOrder = r => require.ensure([], () => r(require('@/pages/mine/order/payOrder')), 'PayOrder') // 支付订单
const Profit = r => require.ensure([], () => r(require('@/pages/mine/profit/profit')), 'Profit') // 我的收益
const Agent = r => require.ensure([], () => r(require('@/pages/mine/agent/agent')), 'Agent') // 家族管理
const CreatePoster = r => require.ensure([], () => r(require('@/pages/mine/poster/createPoster')), 'CreatePoster') // 生成海报
const GetPoster = r => require.ensure([], () => r(require('@/pages/mine/poster/getPoster')), 'GetPoster') // 海报图片
const QrCode = r => require.ensure([], () => r(require('@/pages/mine/poster/qrCode')), 'QrCode') // 二维码
const PersonalInfo = r => require.ensure([], () => r(require('@/pages/user/userInfo/personalInfo')), 'PersonalInfo') // 个人资料
const AddressList = r => require.ensure([], () => r(require('@/pages/user/userInfo/addressList')), 'AddressList') // 地址列表
const AddAuditAddress = r => require.ensure([], () => r(require('@/pages/user/userInfo/add-audit-address')), 'AddAuditAddress') // 地址列表

const wxRouters = [
  { path: '/register', name: 'register', meta: { title: '注册' }, component: Register },
  { path: '/login', name: 'login', meta: { title: '登录' }, component: Login },
  { path: '/agentProtocol', name: 'agentProtocol', meta: { title: '代理协议' }, component: AgentProtocol },
  { path: '/aboutUs', name: 'aboutUs', meta: { title: '关于我们' }, component: AboutUs },
  { path: '/goodsList', name: 'goodsList', meta: { title: '俞姐姐' }, component: GoodsList },
  { path: '/goodsDetail', name: 'goodsDetail', meta: { title: '商品详情' }, component: GoodsDetail },
  { path: '/mine', name: 'mine', meta: { title: '我的' }, component: Mine },
  { path: '/personalOrder', name: 'personalOrder', meta: { title: '个人订单' }, component: PersonalOrder },
  { path: '/orderDetail', name: 'orderDetail', meta: { title: '订单详情' }, component: OrderDetail },
  { path: '/payOrder', name: 'payOrder', meta: { title: '订单详情' }, component: PayOrder },
  { path: '/profit', name: 'profit', meta: { title: '收益金额' }, component: Profit },
  { path: '/agent', name: 'agent', meta: { title: '家族管理' }, component: Agent },
  { path: '/createPoster', name: 'createPoster', meta: { title: '生成海报' }, component: CreatePoster },
  { path: '/getPoster', name: 'getPoster', meta: { title: '生成海报' }, component: GetPoster },
  { path: '/qrCode', name: 'qrCode', meta: { title: '俞姐姐' }, component: QrCode },
  { path: '/personalInfo', name: 'personalInfo', meta: { title: '个人资料' }, component: PersonalInfo },
  { path: '/addressList', name: 'addressList', meta: { title: '地址管理' }, component: AddressList },
  { path: '/add-audit-address', name: 'add-audit-address', meta: { title: '地址管理' }, component: AddAuditAddress }
]

export default wxRouters

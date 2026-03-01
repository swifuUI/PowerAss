module.exports = { // actcampus
	PROJECT_COLOR: '#A1185E',
	NAV_COLOR: '#ffffff',
	NAV_BG: '#A1185E',

	// setup
	SETUP_CONTENT_ITEMS: [
		{ title: '关于我们', key: 'SETUP_CONTENT_ABOUT' },
		{ title: '用户注册使用协议', key: 'SETUP_YS' }
	],

	// 用户 
	USER_FIELDS: [

	],


	NEWS_NAME: '公告',
	NEWS_CATE: [
		{ id: 1, title: '公告通知' },
		{ id: 2, title: '校园导览' },
		{ id: 3, title: '社团推介' },
		{ id: 4, title: '社团之星' },
	],
	NEWS_FIELDS: [
		{ mark: 'desc', type: 'textarea', title: '简介', must: true, min: 2, max: 200 },
		{ mark: 'content', title: '详细内容', type: 'content', must: true },
		{ mark: 'cover', type: 'image', title: '封面图', must: true, min: 1, max: 1 },
	],


	ACTIVITY_NAME: '活动',
	ACTIVITY_CATE: [
		{ id: 1, title: '招新报名' },
		{ id: 2, title: '社团活动' },
		{ id: 3, title: '讲座培训' },
		{ id: 4, title: '志愿者活动' },

	],
	ACTIVITY_FIELDS: [
		{ mark: 'cover', title: '活动封面', type: 'image', min: 1, max: 1, must: true },
		{ mark: 'desc', title: '活动内容', type: 'content', must: true },
	],
	ACTIVITY_JOIN_FIELDS: [
		{ mark: 'name', type: 'text', title: '姓名', must: true, max: 30 },
		{ mark: 'phone', type: 'mobile', title: '手机', must: true, edit: false }
	],


}
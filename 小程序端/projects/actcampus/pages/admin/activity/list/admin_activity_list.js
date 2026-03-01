const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const ActivityBiz = require('../../../../biz/activity_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');
const projectSetting = require('../../../../public/project_setting.js');

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		isLoad: false,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: async function (options) {
		if (!AdminBiz.isAdmin(this)) return;

		wx.setNavigationBarTitle({
			title: projectSetting.ACTIVITY_NAME + '-管理',
		});
		this.setData({
			ACTIVITY_NAME: projectSetting.ACTIVITY_NAME
		});

		//设置搜索菜单
		this._getSearchMenu();

	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () { },

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: async function () { },

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () { },

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () { },

	url: async function (e) {
		pageHelper.url(e, this);
	},

	bindCommListCmpt: function (e) {
		pageHelper.commListListener(this, e);
	},

	bindJoinMoreTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let itemList = ['报名名单管理', '导出名单Excel表格', '管理员核销报名码', '清空报名数据'];

		let activityId = pageHelper.dataset(e, 'id');
		let title = encodeURIComponent(pageHelper.dataset(e, 'title'));

		wx.showActionSheet({
			itemList,
			success: async res => {
				switch (res.tapIndex) {
					case 0: {
						wx.navigateTo({
							url: '../join_list/admin_activity_join_list?activityId=' + activityId + '&title=' + title,
						});
						break;
					}
					case 1: {
						wx.navigateTo({
							url: '../export/admin_activity_export?activityId=' + activityId + '&title=' + title,
						});
						break;
					}
					case 2: {
						wx.navigateTo({
							url: '../scan/admin_activity_scan?activityId=' + activityId + '&title=' + title,
						});
						break;
					}
					case 3: {
						this._clear(e);
					}
				}
			},
			fail: function (res) { }
		})
	},

	_clear: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let activityId = pageHelper.dataset(e, 'id');

		let params = {
			activityId
		}

		let callback = async () => {
			try {
				let opts = {
					title: '处理中'
				}
				await cloudHelper.callCloudSumbit('admin/activity/join/clear', params, opts).then(res => {
					let node = {
						'activityJoinCnt': 0,
					}
					pageHelper.modifyPrevPageListNodeObject(activityId, node, 1, 'dataList', 'activityId');

					pageHelper.showSuccToast('清空完成');
				});
			} catch (err) {
				console.log(err);
			}
		}
		pageHelper.showConfirm('确认清空所有数据？清空后不可恢复', callback);

	},

	bindStatusMoreTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let itemList = ['启用', '停用 (不显示)', '删除'];
		wx.showActionSheet({
			itemList,
			success: async res => {
				switch (res.tapIndex) {
					case 0: { //启用
						e.currentTarget.dataset['status'] = 1;
						await this._setStatus(e);
						break;
					}
					case 1: { //停止 
						e.currentTarget.dataset['status'] = 0;
						await this._setStatus(e);
						break;
					}
					case 2: { //删除
						await this._del(e);
						break;
					}
				}
			},
			fail: function (res) { }
		})
	},

	bindMoreTap: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let idx = pageHelper.dataset(e, 'idx');

		let order = this.data.dataList.list[idx].activityOrder;
		let orderDesc = (order == 0) ? '取消置顶' : '置顶';


		let vouch = this.data.dataList.list[idx].activityVouch;
		let vouchDesc = (vouch == 0) ? '推荐到首页' : '取消首页推荐';

		let itemList = ['预览', orderDesc, vouchDesc];
		let id = pageHelper.dataset(e, 'id');

		wx.showActionSheet({
			itemList,
			success: async res => {
				switch (res.tapIndex) {
					case 0: { //预览 
						wx.navigateTo({
							url: '../../../activity/detail/activity_detail?id=' + id,
						});
						break;
					}
					case 1: { //置顶 
						order = (order == 0) ? 9999 : 0;

						e.currentTarget.dataset['order'] = order;
						await this._setOrder(e);
						break;
					}
					case 2: { //上首页 
						vouch = (vouch == 0) ? 1 : 0;
						e.currentTarget.dataset['vouch'] = vouch;
						await this._setVouch(e);
						break;
					}
				}


			},
			fail: function (res) { }
		})
	},

	_setOrder: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;

		let id = pageHelper.dataset(e, 'id');
		let order = pageHelper.dataset(e, 'order');
		if (!id) return;

		let params = {
			id,
			order
		}

		try {
			await cloudHelper.callCloudSumbit('admin/activity/order', params).then(res => {
				pageHelper.modifyListNode(id, this.data.dataList.list, 'activityOrder', order, 'activityId');
				this.setData({
					dataList: this.data.dataList
				});
				pageHelper.showSuccToast('设置成功');
			});
		} catch (err) {
			console.log(err);
		}
	},

	_setVouch: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;

		let id = pageHelper.dataset(e, 'id');
		let vouch = pageHelper.dataset(e, 'vouch');
		if (!id) return;

		let params = {
			id,
			vouch
		}

		try {
			await cloudHelper.callCloudSumbit('admin/activity/vouch', params).then(res => {
				pageHelper.modifyListNode(id, this.data.dataList.list, 'activityVouch', vouch, 'activityId');
				this.setData({
					dataList: this.data.dataList
				});
				pageHelper.showSuccToast('设置成功');
			});
		} catch (err) {
			console.log(err);
		}
	},

	_del: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let id = pageHelper.dataset(e, 'id');

		let params = {
			id
		}

		let callback = async () => {
			try {
				let opts = {
					title: '删除中'
				}
				await cloudHelper.callCloudSumbit('admin/activity/del', params, opts).then(res => {
					pageHelper.delListNode(id, this.data.dataList.list, 'activityId');
					this.data.dataList.total--;
					this.setData({
						dataList: this.data.dataList
					});
					pageHelper.showSuccToast('删除成功');
				});
			} catch (err) {
				console.log(err);
			}
		}
		pageHelper.showConfirm('确认删除？删除后报名数据将一并删除且不可恢复', callback);

	},

	_setStatus: async function (e) {
		if (!AdminBiz.isAdmin(this)) return;
		let id = pageHelper.dataset(e, 'id');
		let status = Number(pageHelper.dataset(e, 'status'));
		let params = {
			id,
			status
		}

		try {
			await cloudHelper.callCloudSumbit('admin/activity/status', params).then(res => {
				pageHelper.modifyListNode(id, this.data.dataList.list, 'activityStatus', status, 'activityId');
				pageHelper.modifyListNode(id, this.data.dataList.list, 'statusDesc', res.data.statusDesc, 'activityId');
				this.setData({
					dataList: this.data.dataList
				});
				pageHelper.showSuccToast('设置成功');
			});
		} catch (err) {
			console.log(err);
		}
	},

	_getSearchMenu: function () {
		let cateIdOptions = ActivityBiz.getCateList();

		let sortItem1 = [{ label: '分类', type: '', value: 0 }];
		sortItem1 = sortItem1.concat(cateIdOptions);

		let sortItem2 = [
			{ label: '排序', type: '', value: 0 },
			{ label: '按报名人数', type: 'sort', value: 'ACTIVITY_JOIN_CNT|desc' },
			{ label: '按开始时间', type: 'sort', value: 'ACTIVITY_START|asc' },
			{ label: '浏览量▽', type: 'sort', value: 'ACTIVITY_VIEW_CNT|desc' },
			{ label: '浏览量△', type: 'sort', value: 'ACTIVITY_VIEW_CNT|asc' },
		];

		let sortItems = [];
		if (sortItem1.length > 2) sortItems.push(sortItem1);
		sortItems.push(sortItem2);

		let sortMenus = [
			{ label: '全部', type: '', value: '' },
			{ label: '正常', type: 'status', value: 1 },
			{ label: '停用', type: 'status', value: 0 },
			{ label: '最新', type: 'sort', value: 'new' },
			{ label: '首页推荐', type: 'vouch', value: 'vouch' },
			{ label: '置顶', type: 'top', value: 'top' },
		]
		this.setData({
			search: '',
			cateIdOptions,
			sortItems,
			sortMenus,
			isLoad: true
		})
	}

})
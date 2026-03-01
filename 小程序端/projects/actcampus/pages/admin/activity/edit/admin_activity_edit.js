const AdminBiz = require('../../../../../../comm/biz/admin_biz.js');
const ProjectBiz = require('../../../../biz/project_biz.js');
const pageHelper = require('../../../../../../helper/page_helper.js');
const cloudHelper = require('../../../../../../helper/cloud_helper.js');
const dataHelper = require('../../../../../../helper/data_helper.js');
const validate = require('../../../../../../helper/validate.js');
const ActivityBiz = require('../../../../biz/activity_biz.js');
const AdminActivityBiz = require('../../../../biz/admin_activity_biz.js');
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
		if (!pageHelper.getOptions(this, options)) return;

		wx.setNavigationBarTitle({
			title: projectSetting.ACTIVITY_NAME + '-修改',
		});

		this._loadDetail();
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () { },

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () { },

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () { },

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () { },

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: async function () {
		await this._loadDetail();
		this.selectComponent("#cmpt-form").reload();
		wx.stopPullDownRefresh();
	},

	model: function (e) {
		pageHelper.model(this, e);
	},

	_loadDetail: async function () {
		if (!AdminBiz.isAdmin(this)) return;

		let id = this.data.id;
		if (!id) return;

		if (!this.data.isLoad) this.setData(AdminActivityBiz.initFormData(id)); // 初始化表单数据

		let params = {
			id
		};
		let opt = {
			title: 'bar'
		};
		let activity = await cloudHelper.callCloudData('admin/activity/detail', params, opt);
		if (!activity) {
			this.setData({
				isLoad: null
			})
			return;
		};


		this.setData({
			isLoad: true,

			formTitle: activity.activityTitle,
			formCateId: activity.activityCateId,
			formOrder: activity.activityOrder,

			formMaxCnt: activity.activityMaxCnt,
			formStart: activity.activityStart,
			formEnd: activity.activityEnd,
			formStop: activity.activityStop,

			formAddress: activity.activityAddress,
			formAddressGeo: JSON.parse(activity.activityAddressGeo),

			formForms: JSON.parse(activity.activityForms),

		});

	},

	bindFormSubmit: async function () {
		if (!AdminBiz.isAdmin(this)) return;

		// 数据校验
		let data = this.data;
		data = validate.check(data, AdminActivityBiz.CHECK_FORM, this);
		if (!data) return;

		if (data.end < data.start) {
			return pageHelper.showModal('结束时间不能早于开始时间');
		}

		let forms = this.selectComponent("#cmpt-form").getForms(true);
		if (!forms) return;

		data.forms = JSON.stringify(forms);
		data.obj = JSON.stringify(dataHelper.dbForms2Obj(forms));

		data.addressGeo = JSON.stringify(data.addressGeo);
		data.cateName = ActivityBiz.getCateName(data.cateId);

		try {
			let activityId = this.data.id;
			data.id = activityId;

			// 先修改，再上传 
			await cloudHelper.callCloudSumbit('admin/activity/edit', data).then(res => {
				// 更新列表页面数据
				let node = {
					'activityTitle': data.title,
					'activityCateName': data.cateName,
					'activityOrder': data.order,
					'activityStart': data.start,
					'activityEnd': data.end,
					'activityMaxCnt': data.maxCnt,
					statusDesc: res.data.statusDesc
				}
				pageHelper.modifyPrevPageListNodeObject(activityId, node, 2, 'dataList', 'activityId');
			});


			let callback = () => {
				wx.navigateBack();
			}
			pageHelper.showSuccToast('修改成功', 2000, callback);

		} catch (err) {
			console.log(err);
		}

	},

	bindMapTap: function (e) {
		ProjectBiz.selectLocation(this);
	},

	url: function (e) {
		pageHelper.url(e, this);
	},

	switchModel: function (e) {
		pageHelper.switchModel(this, e);
	},


})
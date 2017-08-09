$(function() {
	InitLeftMenu();
	tabClose();
	tabCloseEven();
});

// 初始化左侧
function InitLeftMenu() {

	$(".easyui-accordion").empty();
	var menulist = "";

	$.each(_menus.menus, function(i, n) {
		menulist += '<div title="' + n.menuname
				+ '"  data-options="" style="overflow:auto;">';
		menulist += '<ul>';
		$.each(n.menus, function(j, o) {
			menulist += '<li><div><a target="mainFrame" href="' + o.url
					+ '" ><span class="icon ' + o.icon + '" ></span>'
					+ o.menuname + '</a></div></li> ';
		})
		menulist += '</ul></div>';
	})
	$("#divAcc").append(menulist);
	$('#divAcc').accordion({
		fit : true,
		border : false
	});
	$("#test").click(function() {
		addParentTab('', 'http://www.baidu.com');
	});
	$('#divAcc li a').click(function() {
		var tabTitle = $(this).text();
		var url = $(this).attr("href");
		addTab(tabTitle, url);
		// $('#tabs').find('.panel-body').css("overflow", "hidden");
		$('#divAcc li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function() {
		$(this).parent().addClass("hover");
	}, function() {
		$(this).parent().removeClass("hover");
	});

}
function addParentTab(subtitle, url) {
	parent$ = self.parent.$;
	if (!parent$('#tabs').tabs('exists', subtitle)) {
		parent$('#tabs').tabs('add', { // 实现add方法
			title : subtitle,
			content : createFrame(url),
			
			closable : true,
			width : $('#mainPanle').width() - 10,
			height : $('#mainPanle').height() - 26
		});
		
	}else {
		parent$('#tabs').tabs('select', subtitle);
	}
	tabClose();
}
function addTab(subtitle, url) {
	if (!$('#tabs').tabs('exists', subtitle)) {
		$('#tabs').tabs('add', {
			title : subtitle,
			content : createFrame(url),

			closable : true,
			width : $('#mainPanle').width() - 10,
			height : $('#mainPanle').height() - 26
		});
	} else {
		$('#tabs').tabs('select', subtitle);
	}
	tabClose();
}

function createFrame(url) {
	var s = '<iframe name="mainFrame" scrolling="auto" frameborder="0"  src="'
			+ url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/* 双击关闭TAB选项卡 */
	$(".tabs-inner").dblclick(function() {
		var subtitle = $(this).children("span").text();
		$('#tabs').tabs('close', subtitle);
	})

	$(".tabs-inner").bind('contextmenu', function(e) {
		e.preventDefault();
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY,
		});

		var subtitle = $(this).children("span").text();
		$('#mm').data("currtab", subtitle);

		return false;
	});
}
// 绑定右键菜单事件
function tabCloseEven() {
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close', currtab_title);
	})
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			$('#tabs').tabs('close', t);
		});
	});
	// 关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function() {
		var currtab_title = $('#mm').data("currtab");
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			if (t != currtab_title)
				$('#tabs').tabs('close', t);
		});
	});
	// 关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function() {
		var nextall = $('.tabs-selected').nextAll();
		if (nextall.length == 0) {
			// msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});
	// 关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function() {
		var prevall = $('.tabs-selected').prevAll();
		if (prevall.length == 0) {
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i, n) {
			var t = $('a:eq(0) span', $(n)).text();
			$('#tabs').tabs('close', t);
		});
		return false;
	});

	// 退出
	$("#mm-exit").click(function() {
		$('#mm').menu('hide');
	})
}

// 弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

function clockon() {
	var now = new Date();
	var year = now.getFullYear(); // getFullYear getYear
	var month = now.getMonth();
	var date = now.getDate();
	var day = now.getDay();
	var hour = now.getHours();
	var minu = now.getMinutes();
	var sec = now.getSeconds();
	var week;
	month = month + 1;
	if (month < 10)
		month = "0" + month;
	if (date < 10)
		date = "0" + date;
	if (hour < 10)
		hour = "0" + hour;
	if (minu < 10)
		minu = "0" + minu;
	if (sec < 10)
		sec = "0" + sec;
	var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
	week = arr_week[day];
	var time = "";
	time = year + "年" + month + "月" + date + "日" + " " + hour + ":" + minu
			+ ":" + sec + " " + week;

	$("#bgclock").html(time);

	var timer = setTimeout("clockon()", 200);
}

var _menus = {
	"menus" : [ {
		"menuid" : "1",
		"icon" : "icon-sys",
		"menuname" : "系统管理",
		"menus" : [ {
			"menuname" : "菜单管理",
			"icon" : "icon-nav",
			"url" : "/menu/home"
		}, {
			"menuname" : "用户管理",
			"icon" : "icon-users",
			"url" : "/user/home"
		}, {
			"menuname" : "角色管理",
			"icon" : "icon-role",
			"url" : "/role/home"
		}, {
			"menuname" : "权限设置",
			"icon" : "icon-set",
			"url" : "/permission/home"
		}, {
			"menuname" : "系统日志",
			"icon" : "icon-log",
			"url" : "demo.html"
		} ]
	}, {
		"menuid" : "8",
		"icon" : "icon-sys",
		"menuname" : "员工管理",
		"menus" : [ {
			"menuname" : "员工列表",
			"icon" : "icon-nav",
			"url" : "demo.html"
		}, {
			"menuname" : "视频监控",
			"icon" : "icon-nav",
			"url" : "demo1.html"
		} ]
	}, {
		"menuid" : "56",
		"icon" : "icon-sys",
		"menuname" : "部门管理",
		"menus" : [ {
			"menuname" : "添加部门",
			"icon" : "icon-nav",
			"url" : "demo1.html"
		}, {
			"menuname" : "部门列表",
			"icon" : "icon-nav",
			"url" : "demo2.html"
		} ]
	}, {
		"menuid" : "28",
		"icon" : "icon-sys",
		"menuname" : "财务管理",
		"menus" : [ {
			"menuname" : "收支分类",
			"icon" : "icon-nav",
			"url" : "demo.html"
		}, {
			"menuname" : "报表统计",
			"icon" : "icon-nav",
			"url" : "demo1.html"
		}, {
			"menuname" : "添加支出",
			"icon" : "icon-nav",
			"url" : "demo.html"
		} ]
	}, {
		"menuid" : "39",
		"icon" : "icon-sys",
		"menuname" : "商城管理",
		"menus" : [ {
			"menuname" : "商品分",
			"icon" : "icon-nav",
			"url" : "/shop/productcatagory.aspx"
		}, {
			"menuname" : "商品列表",
			"icon" : "icon-nav",
			"url" : "/shop/product.aspx"
		}, {
			"menuname" : "商品订单",
			"icon" : "icon-nav",
			"url" : "/shop/orders.aspx"
		} ]
	} ]
};

var formInfo = {
	grid : '#mytable',
	form : '#InfoForm',

	// 添加模块
	addFormInfo : function(title, submitUrl) {
		var d = this.createFormInfoDialog(submitUrl);
		d.dialog({
			title : title
		}).dialog('open');
	},

	// 保存模块
	saveFormInfo : function(submitUrl) {
		$(formInfo.form).form('submit', {
			url : submitUrl,
			onSubmit : function() {
				var flag = $(this).form('enableValidation').form('validate');
				if (flag) {
					Ext.progress('Loading...');
				}
				return flag;
			},
			success : function(result) {
				result = $.parseJSON(result);
				if (result.errorCode == 0) {
					var d = formInfo.createFormInfoDialog(submitUrl);
					d.dialog('close');
					$(formInfo.grid).datagrid("reload");
				} else {
					Ext.alert(result.errorText);
				}

				Ext.progressClose();
			}
		});
	},
	// 模块框
	createFormInfoDialog : function(submitUrl) {
		$(this.form).form('clear');
		$('#InfoForm input[name=id]').val(0);

		var d = $('#InfoDialog').dialog({
			width : 500,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			resizable : false,
			modal : true,
			iconCls : 'icon-win',
			buttons : [ {
				text : 'Save',
				handler : function() {
					formInfo.saveFormInfo(submitUrl);
				}
			// this.saveFormInfo
			}, {
				text : 'Close',
				handler : function() {
					d.dialog('close');
				}
			} ]
		});
		return d;
	},

	// 修改模块弹出修改框
	updateInfo : function(title, updateUrl) {
		var message = Ext.getSingleSelected(this.grid);
		if (message) {
			return Ext.alert(message);
		}

		this.update(title, updateUrl);
	},

	// 执行更新操作
	update : function(title, updateUrl) {
		var record = Ext.getRecord(formInfo.grid);
		var d = this.createFormInfoDialog(updateUrl);
		d.dialog({
			title : title
		}).dialog('open');

		// 加载form表单
		$(this.form).form('load', record);
		// $('#menuInfoForm input[name=id]').val(1);
	},
	// 删除模块
	deleteInfo : function(deleteUrl) {
		var message = Ext.getSingleSelected(this.grid);
		if (message) {
			return Ext.alert(message);
		}

		Ext.confirm('您确认要删除这条记录吗?', function() {
			Ext.progress('Loading...');
			var record = Ext.getRecord(formInfo.grid);
			$.get(deleteUrl + record.id, function(result) {
				result = $.parseJSON(result);
				if (result.errorCode == 0) {
					$(formInfo.grid).datagrid("reload")
				} else {
					Ext.alert(result.errorText);
				}
				Ext.progressClose();
			});
		});
	},
	editPermission : function() {
		var message = Ext.getSingleSelected(formInfo.grid);
		if (message) {
			return Ext.alert(message);
		}
		var record = Ext.getRecord(formInfo.grid);
		var url = '/permission/home?roleId=' + record.id;
		$('#tabs').tabs('add', {
			title : 'New Tab',
			content : 'Tab Body',
			closable : true,
			tools : [ {
				iconCls : 'icon-mini-refresh',
				handler : function() {
					alert('refresh');
				}
			} ]
		});
		alert('fail');
	},
	// 模块详情
	detailTaskInfo : function(index) {
		$(TaskInfo.grid).datagrid('selectRow', index);
		var row = Ext.getRecord(this.grid);
		if (!row) {
			return;
		}

		// 弹出Dialog, 并修改Title和隐藏Button
		var d = this.createFormInfoDialog();
		d.dialog({
			title : "Detail Job"
		}).dialog('open');
		$(".dialog-button a").eq(0).hide();
		$(this.form).form('load', row)
	},
};
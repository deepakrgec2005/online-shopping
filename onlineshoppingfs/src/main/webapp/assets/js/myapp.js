$(function() {

	// for adding a loader
	/*
	 * $(window).load(function(){ setTimeout(function() {
	 * $(".se-pre-con").fadeOut("slow"); }, 500); });
	 *  // for handling CSRF token var token =
	 * $('meta[name="_csrf"]').attr('content'); var header =
	 * $('meta[name="_csrf_header"]').attr('content');
	 * 
	 * if((token!=undefined && header !=undefined) && (token.length > 0 &&
	 * header.length > 0)) { // set the token header for the ajax request
	 * $(document).ajaxSend(function(e, xhr, options) {
	 * xhr.setRequestHeader(header,token); }); }
	 * 
	 */

	// solving the active menu problem
	switch (menu) {

	case 'About Us':
		$('.about').addClass('active');
		break;
	case 'Contact Us':
		$('.contact').addClass('active');
		break;
	case 'All Products':
		$('.listProducts').addClass('active');
		break;
	case 'Product Management':
		$('.manageProducts').addClass('active');
		break;
	case 'User Cart':
		$('#userCart').addClass('active');
		break;
	default:
		if (menu == "Home")
			break;
		$('#listProducts').addClass('active');
		$('#a_' + menu).addClass('active');
		break;
	}

	var token = $('meta[name="_csrf"]').attr('content');
	var header = $('meta[name="_csrf_header"]').attr('content');

	if (token.length > 0 && header.length > 0) {
		$(document).ajaxSend(function(e, xhr, options) {
			xhr.setRequestHeader(header, token);
		});
	}

	// code for jquery dataTable
	var $table = $('#productListTable');

	// execute the below code only where we have this table
	if ($table.length) {
		// console.log('Inside the table!');

		var jsonUrl = '';
		if (window.categoryId == '') {
			jsonUrl = window.contextRoot + '/json/data/all/products';
		} else {
			jsonUrl = window.contextRoot + '/json/data/category/'
					+ window.categoryId + '/products';
		}

		$table
				.DataTable({
					lengthMenu : [ [ 3, 5, 10, -1 ],
							[ '3 Records', '5 Records', '10 Records', 'ALL' ] ],
					pageLength : 5,
					ajax : {
						url : jsonUrl,
						dataSrc : ''
					},
					columns : [
							{
								data : 'code',

								mRender : function(data, type, row) {
									return '<img src="' + window.contextRoot
											+ '/resources/images/' + data
											+ '.jpg" class="dataTableImg"/>';
								}
							},

							{
								data : 'name'
							},
							{
								data : 'brand'
							},
							{
								data : 'unitPrice',
								mRender : function(data, type, row) {
									return '&#8377; ' + data
								}
							},
							{
								data : 'quantity',
								mRender : function(data, type, row) {
									if (data < 1) {
										return '<span style="color:red"> Out of Stock!</span>';
									}

									return data;
								}
							},
							{
								data : 'id',
								bSortable : false,
								mRender : function(data, type, row) {
									var str = '';
									str += '<a href="'
											+ window.contextRoot
											+ '/show/'
											+ data
											+ '/product" class="btn btn-primary"><span class="glyphicon glyphicon-eye-open"></span></a> &#160;';

									if (userRole == 'ADMIN') {
										str += '<a href="'
												+ window.contextRoot
												+ '/manage/'
												+ data
												+ '/product" class="btn btn-warning"><span class="glyphicon glyphicon-pencil"></span></a>';

									} else {

										if (row.quantity < 1) {
											str += '<a href="javascript:void(0)" class="btn btn-success disabled"><span class="glyphicon glyphicon-shopping-cart"></span></a>';
										} else {

											str += '<a href="'
													+ window.contextRoot
													+ '/cart/add/'
													+ data
													+ '/product" class="btn btn-success"><span class="glyphicon glyphicon-shopping-cart"></span></a>';

										}

									}

									return str;
								}
							} ]
				});
	}

	var $alert = $('.alert');
	if ($alert.length) {
		setTimeout(function() {
			$alert.fadeOut('slow');
		}, 3000);
	}

	// list of all products for admin
	$('.switch input[type="checkbox"]')
			.on(
					'change',
					function() {

						var checkbox = $(this);
						var checked = checkbox.prop('checked');
						var dMsg = (checked) ? 'You want to activate the product?'
								: 'You want to deactive the product';
						var value = checkbox.prop('value');
						bootbox
								.confirm({
									size : 'medium',
									title : 'Product Activation & Deactivation',
									message : dMsg,
									callback : function(confirmed) {
										if (confirmed) {
											console.log(value);
											bootbox
													.alert({
														size : 'medium',
														title : 'Information',
														message : 'You are going to perform operation on product'
																+ value
													});
										} else {
											checkbox.prop('checked', !checked);
										}
									}
								});
					});
	// code for jquery dataTable
	var $adminProductsTable = $('#adminProductsTable');

	// execute the below code only where we have this tableSf

	if ($adminProductsTable.length) {
		var jsonUrl = window.contextRoot + '/json/data/admin/all/products';
		console.log(jsonUrl);
		$adminProductsTable
				.DataTable({
					lengthMenu : [ [ 10, 30, 50, -1 ],
							[ '10 Records', '30 Records', '50 Records', 'ALL' ] ],
					pageLength : 30,
					ajax : {
						url : jsonUrl,
						dataSrc : ''
					},
					columns : [
							{
								data : 'id'
							},

							{
								data : 'code',
								bSortable : false,
								mRender : function(data, type, row) {
									return '<img src="'
											+ window.contextRoot
											+ '/resources/images/'
											+ data
											+ '.jpg" class="adminDataTableImg"/>';
								}
							},
							{
								data : 'name'
							},
							{
								data : 'quantity',
								mRender : function(data, type, row) {

									if (data < 1) {
										return '<span style="color:red">Out of Stock!</span>';
									}

									return data;

								}
							},
							{
								data : 'unitPrice',
								mRender : function(data, type, row) {
									return '&#8377; ' + data
								}
							},
							{
								data : 'active',
								bSortable : false,
								mRender : function(data, type, row) {
									var str = '';
									if (data) {
										str += '<label class="switch"> <input type="checkbox" value="'
												+ row.id
												+ '" checked="checked">  <div class="slider round"> </div></label>';
									} else {
										str += '<label class="switch"> <input type="checkbox" value="'
												+ row.id
												+ '">  <div class="slider round"> </div></label>';
									}

									return str;
								}
							},
							{
								data : 'id',
								bSortable : false,
								mRender : function(data, type, row) {
									var str = '';
									str += '<a href="'
											+ window.contextRoot
											+ '/manage/'
											+ data
											+ '/product" class="btn btn-primary"><span class="glyphicon glyphicon-pencil"></span></a> &#160;';
									return str;
								}
							} ],
					initComplete : function() {
						var api = this.api();
						api
								.$('.switch input[type="checkbox"]')
								.on(
										'change',
										function() {
											var checkbox = $(this);
											var checked = checkbox
													.prop('checked');
											var dMsg = (checked) ? 'You want to activate the product?'
													: 'You want to deactive the product';
											var value = checkbox.prop('value');
											bootbox
													.confirm({
														size : 'medium',
														title : 'Product Activation/Deactivation',
														message : dMsg,
														callback : function(
																confirmed) {
															if (confirmed) {

																console
																		.log(value);
																var activationUrl = window.contextRoot
																		+ '/manage/product/'
																		+ value
																		+ '/activation';
																$
																		.post(
																				activationUrl,
																				function(
																						data) {
																					bootbox
																							.alert({
																								size : 'medium',
																								title : 'Information',
																								message : data
																							});

																				});

															} else {
																checkbox
																		.prop(
																				'checked',
																				!checked);
															}
														}
													});
										});
					}
				});
	}

	// -----------

	var $categoryForm = $('#categoryForm');
	if ($categoryForm.length) {

		$categoryForm
				.validate({

					rules : {
						name : {
							required : true,
							minlength : 2
						},
						description : {
							reuired : true

						}
					},
					message : {
						name : {
							required : 'Please add the Category name!',
							minlength : 'The category name should not be less than 2 characters'
						},
						description : {
							required : 'Please add a descriotion for this category!'
						}

					},
					errorElement : 'em',
					errorPlacement : function(error, element) {
						error.addClass('help-block');
						error: insertAfter(element);
					}
				});
	}

	// ------

	// -----------

	/*
	 * To be check
	 * var loginForm = $('#loginForm');
	if ($loginForm.length) {

		$loginForm.validate({

			rules : {
				username : {
					required : true,
					email : true
				},
				password : {
					required : true

				}
			},
			message : {
				username : {
					required : 'Please enter the username!',
					email : 'Please enter valid email address'
				},
				password : {
					required : 'Please enter the password!'
				}

			},
			errorElement : 'em',
			errorPlacement : function(error, element) {
				error.addClass('help-block');
				error: insertAfter(element);
			}
		});
	}*/

	// ------

	$('button[name="refreshCart"]').click(function() {
		var cartLineId = $(this).attr('value');
		var countElement = $('#count_' + cartLineId);
		var originalCount = countElement.attr('value');    
		var currentCount = countElement.val();
		if (currentCount !== originalCount) {

			/*console.log("current count:" + currentCount);
			console.log("Original count:" + originalCount);*/
			 
			
			  if(currentCount <1 ||currentCount >3) {
			  countElement.val(originalCount); 
			  bootbox.alert({
				  size:'medium', title: 'Error', message:'Product count should be minimum a and maximum 3!'
			  			  }); 
			  } else{
			  var updateUrl = window.contextRoot +'/cart/'+cartLineId+'/update?count='+ currentCount;
			 
			  window.location.href=updateUrl;
			  }
			 
		}

	});

});
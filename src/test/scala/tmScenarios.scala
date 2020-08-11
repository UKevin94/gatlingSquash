
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import java.util.concurrent.atomic.AtomicInteger

class tmScenarios extends Simulation {

	val httpProtocol = http
		.baseUrls("http://url1", "http://url2", "http://url3")
		.inferHtmlResources()
		.acceptHeader("application/json, text/javascript, */*; q=0.01")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0")

	val default_header = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val image_header = Map("Accept" -> "image/webp,*/*")

	val css_header = Map("Accept" -> "text/css,*/*;q=0.1")

	val no_content_header = Map(
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

	val accept_json_header = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

    val accept_all_header = Map(
		"Accept" -> "*/*",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")
		
	val content_json_header = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"Content-Type" -> "application/json;charset=UTF-8",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

	val form_content_header = Map(
		"Content-Type" -> "application/x-www-form-urlencoded; charset=UTF-8",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

    val newUserId = new AtomicInteger(0)
    
    val newReqCount = new AtomicInteger(0)

object Login {

    val login = exec(http("openSquashTM")
                .get("/squash/")
                .headers(default_header)
                .check(css("meta[name='_csrf']","content").saveAs("csrf_token"))).exitHereIfFailed
            .pause(8)
            .exec(http("loginToTM")
                .post("/squash/login")
                .headers(default_header)
                .formParam("_csrf", "${csrf_token}")
                .formParam("username", "admin")
                .formParam("password", "admin")).exitHereIfFailed
            .pause(5)
}

object TestCase {

    val goToTestCaseWorkspace = exec(http("openTCWorkspace")
			.get("/squash/test-case-workspace/")
			.headers(default_header)
			.check(css("meta[name='_csrf']","content").saveAs("csrf_token"))
			.resources(http("getSecondTC")
			.get("/squash/test-cases/2")
			.headers(accept_all_header),
            http("getAttachmentListForTCTwo")
			.get("/squash/attach-list/5312/attachments/details?sEcho=1&iColumns=5&sColumns=%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=entity-index&bSortable_0=false&mDataProp%5B1%5D=hyphenated-name&bSortable_1=true&mDataProp%5B2%5D=size&bSortable_2=true&mDataProp%5B3%5D=added-on&bSortable_3=true&mDataProp%5B4%5D=empty-delete-holder&bSortable_4=false&iSortCol%5B0%5D=1&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596701202677")
			.headers(no_content_header),
            http("openSecondTCPanel")
			.get("/squash/test-cases/2/steps/panel")
			.headers(accept_all_header),
            http("getTC2VerifiedRequirementVersions")
			.get("/squash/test-cases/2/verified-requirement-versions?includeCallSteps=true&sEcho=1&iColumns=11&sColumns=%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=entity-index&bSortable_0=false&mDataProp%5B1%5D=project&bSortable_1=true&mDataProp%5B2%5D=entity-id&bSortable_2=true&mDataProp%5B3%5D=reference&bSortable_3=true&mDataProp%5B4%5D=name&bSortable_4=true&mDataProp%5B5%5D=versionNumber&bSortable_5=true&mDataProp%5B6%5D=criticality&bSortable_6=true&mDataProp%5B7%5D=category&bSortable_7=true&mDataProp%5B8%5D=verifyingSteps&bSortable_8=false&mDataProp%5B9%5D=empty-delete-holder&bSortable_9=false&mDataProp%5B10%5D=milestone&bSortable_10=false&iSortCol%5B0%5D=4&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596701202678")
			.headers(no_content_header),
            http("getCustomFieldsBinding")
			.get("/squash/custom-fields-binding?projectId=1&bindableEntity=TEST_STEP&optional=false")
			.headers(no_content_header))).exitHereIfFailed
		.pause(6)
		
		
		val selectTestCaseOne = exec(http("openFirstTestCase")
			.get("/squash/test-cases/1")
			.headers(accept_all_header)
			.resources(http("closeSecondTestCase")
			.delete("/squash/test-cases/2/opened-entity")
			.headers(accept_all_header),
            http("getAttachmentListForTCOne")
			.get("/squash/attach-list/5311/attachments/details?sEcho=1&iColumns=5&sColumns=%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=entity-index&bSortable_0=false&mDataProp%5B1%5D=hyphenated-name&bSortable_1=true&mDataProp%5B2%5D=size&bSortable_2=true&mDataProp%5B3%5D=added-on&bSortable_3=true&mDataProp%5B4%5D=empty-delete-holder&bSortable_4=false&iSortCol%5B0%5D=1&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596701202679")
			.headers(no_content_header),
            http("openFirstTCPanel")
			.get("/squash/test-cases/1/steps/panel")
			.headers(accept_all_header),
            http("getTC1VerifiedRequirementVersions")
			.get("/squash/test-cases/1/verified-requirement-versions?includeCallSteps=true&sEcho=1&iColumns=11&sColumns=%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=entity-index&bSortable_0=false&mDataProp%5B1%5D=project&bSortable_1=true&mDataProp%5B2%5D=entity-id&bSortable_2=true&mDataProp%5B3%5D=reference&bSortable_3=true&mDataProp%5B4%5D=name&bSortable_4=true&mDataProp%5B5%5D=versionNumber&bSortable_5=true&mDataProp%5B6%5D=criticality&bSortable_6=true&mDataProp%5B7%5D=category&bSortable_7=true&mDataProp%5B8%5D=verifyingSteps&bSortable_8=false&mDataProp%5B9%5D=empty-delete-holder&bSortable_9=false&mDataProp%5B10%5D=milestone&bSortable_10=false&iSortCol%5B0%5D=4&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596701202680")
			.headers(no_content_header),
            http("getCustomFieldsBinding")
			.get("/squash/custom-fields-binding?projectId=1&bindableEntity=TEST_STEP&optional=false")
			.headers(no_content_header)))
}

object Requirement {

    val goToRequirementWorkspace = exec(http("openRequirementWorkspace")
			.get("/squash/requirement-workspace/")
			.headers(default_header)
			.check(css("meta[name='_csrf']","content").saveAs("csrf_token"))
			.resources(http("jquery-ui-icons")
			.get("/squash/styles/jquery/ui/images/ui-icons_ffffff_256x240-342bc03f6264c75d3f1d7f99e34295b9.png")
			.headers(image_header),
            http("requirement-workspace.js")
			.get("/squash/scripts/requirement-workspace.js"),
            http("req-workspace-main.js")
			.get("/squash/scripts/req-workspace/req-workspace-main.js"),
            http("squashtm.workspace.js")
			.get("/squash/scripts/app/ws/squashtm.workspace.js"),
            http("init-all.js")
			.get("/squash/scripts/req-workspace/popups/init-all.js"),
            http("req-treemenu.js")
			.get("/squash/scripts/req-workspace/req-treemenu.js"),
            http("milestones-tree-menu.js")
			.get("/squash/scripts/milestones/milestones-tree-menu.js"),
            http("squashtm.navbar.js")
			.get("/squash/scripts/app/ws/squashtm.navbar.js"),
            http("milestone-activation.js")
			.get("/squash/scripts/milestone-manager/milestone-activation.js"),
            http("squashtm.notification.js")
			.get("/squash/scripts/app/ws/squashtm.notification.js"),
            http("squashtm.toggleworkspace.js")
			.get("/squash/scripts/app/ws/squashtm.toggleworkspace.js"),
            http("WorkspaceTreeMenu.js")
			.get("/squash/scripts/workspace/WorkspaceTreeMenu.js"),
            http("init-actions.js")
			.get("/squash/scripts/req-workspace/init-actions.js"),
            http("squash.tree-page-resizer.js")
			.get("/squash/scripts/squash/squash.tree-page-resizer.js"),
            http("utils.js")
			.get("/squash/scripts/req-workspace/utils.js"),
            http("add-requirement-popup.js")
			.get("/squash/scripts/req-workspace/popups/add-requirement-popup.js"),
            http("add-folder-popup.js")
			.get("/squash/scripts/req-workspace/popups/add-folder-popup.js"),
            http("ProjectFilter.js")
			.get("/squash/scripts/project-filter/ProjectFilter.js"),
            http("export-popup.js")
			.get("/squash/scripts/req-workspace/popups/export-popup.js"),
            http("import-excel-popup.js")
			.get("/squash/scripts/req-workspace/popups/import-excel-popup.js"),
            http("jquery.squash.formdialog.js")
			.get("/squash/scripts/squashtest/jquery.squash.formdialog.js"),
            http("workspace.sessionStorage.js")
			.get("/squash/scripts/workspace/workspace.sessionStorage.js"),
            http("permissions-rules.js")
			.get("/squash/scripts/req-workspace/permissions-rules.js"),
            http("rename-node-popup.js")
			.get("/squash/scripts/req-workspace/popups/rename-node-popup.js"),
            http("user-prefs.js")
			.get("/squash/scripts/user-account/user-prefs.js"),
            http("delete-node-popup.js")
			.get("/squash/scripts/req-workspace/popups/delete-node-popup.js"),
            http("StringUtil.js")
			.get("/squash/scripts/app/util/StringUtil.js"),
            http("ProjectFilterPopup.js")
			.get("/squash/scripts/project-filter/ProjectFilterPopup.js"),
            http("cuf-values-main.js")
			.get("/squash/scripts/custom-field-values/cuf-values-main.js"),
            http("Forms.js")
			.get("/squash/scripts/app/lnf/Forms.js"),
            http("creation-popup-handler.js")
			.get("/squash/scripts/custom-field-values/creation-popup-handler.js"),
            http("workspace.delnode-popup.js")
			.get("/squash/scripts/workspace/workspace.delnode-popup.js"),
            http("WorkspaceWizardMenu.js")
			.get("/squash/scripts/workspace/WorkspaceWizardMenu.js"),
            http("table-handler.js")
			.get("/squash/scripts/custom-field-values/table-handler.js"),
            http("workspace.import-popup.js")
			.get("/squash/scripts/workspace/workspace.import-popup.js"),
            http("workspace.tree-event-handler.js")
			.get("/squash/scripts/workspace/workspace.tree-event-handler.js"),
            http("jquery.squash.popuperror.js")
			.get("/squash/scripts/squashtest/jquery.squash.popuperror.js"),
            http("AbstractProjectFilterPopup.js")
			.get("/squash/scripts/project-filter/AbstractProjectFilterPopup.js"),
            http("information-panels-handlers.js")
			.get("/squash/scripts/custom-field-values/information-panels-handlers.js"),
            http("jquery.squash.buttonmenu.js")
			.get("/squash/scripts/squashtest/jquery.squash.buttonmenu.js"),
            http("jquery.staticCustomfield.js")
			.get("/squash/scripts/custom-field-values/lib/jquery.staticCustomfield.js"),
            http("ProjectFilterModel.js")
			.get("/squash/scripts/project-filter/ProjectFilterModel.js"),
            http("cuf-values-utils.js")
			.get("/squash/scripts/custom-field-values/lib/cuf-values-utils.js"),
            http("jquery.editableCustomfield.js")
			.get("/squash/scripts/custom-field-values/lib/jquery.editableCustomfield.js"),
            http("squashtable-main.js")
			.get("/squash/scripts/squashtable/squashtable-main.js"),
            http("jquery.jeditableCustomfield.js")
			.get("/squash/scripts/custom-field-values/lib/jquery.jeditableCustomfield.js"),
            http("squash.statusfactory.js")
			.get("/squash/scripts/squash/squash.statusfactory.js"),
            http("squashtable.defaults.js")
			.get("/squash/scripts/squashtable/squashtable.defaults.js"),
            http("squashtable.datatype.js")
			.get("/squash/scripts/squashtable/squashtable.datatype.js"),
            http("squashtable.pagination.js")
			.get("/squash/scripts/squashtable/squashtable.pagination.js"),
            http("squashtable.dnd.js")
			.get("/squash/scripts/squashtable/squashtable.dnd.js"),
            http("jquery-ui-icons-2")
			.get("/squash/styles/jquery/ui/images/ui-bg_glass_45_0078ae_1x400-1f3e8261dd459aa20aa6fa2729c83904.png")
			.headers(image_header),
            http("squash.tree.css")
			.get("/squash/styles/squash.tree.css")
			.headers(css_header),
            http("ckeditor-config.js")
			.get("/squash/styles/ckeditor/ckeditor-config.js?t=I3I8"),
            http("editor_gecko.css")
			.get("/squash/scripts/ckeditor/skins/moonocolor/editor_gecko.css?t=I3I8")
			.headers(css_header),
            http("en-gb.js?t=I3I8")
			.get("/squash/scripts/ckeditor/lang/en-gb.js?t=I3I8"),
            http("testCaseTreeIcons-4ade86ee03c4da487a09a1399a7e379c.css")
			.get("/squash/styles/testCaseTreeIcons-4ade86ee03c4da487a09a1399a7e379c.css")
			.headers(css_header),
            http("styles.js?t=I3I8")
			.get("/squash/scripts/ckeditor/styles.js?t=I3I8"),
            http("squashtree-0eb50798dca00f5cc8e153e6da9a87f9.png")
			.get("/squash/images/squashtree-0eb50798dca00f5cc8e153e6da9a87f9.png")
			.headers(image_header),
            http("star_2-03d23f9cb9c72653fdc1b797ab8063c8.png")
			.get("/squash/images/star_2-03d23f9cb9c72653fdc1b797ab8063c8.png")
			.headers(image_header),
            http("dialog.css")
			.get("/squash/scripts/ckeditor/plugins/scayt/dialogs/dialog.css")
			.headers(css_header))).exitHereIfFailed
		.pause(2)
		.exec(http("ui-bg_glass_75.png")
			.get("/squash/styles/images/ui-bg_glass_75_2b2b2b_1x400-7bf1397b640453b0f8fe4730546a298d.png")
			.headers(image_header)
			.resources(http("ui-bg_glass_45.png")
			.get("/squash/styles/images/ui-bg_glass_45_2b2b2b_1x400-8269426d842770c190b9ef4fbae48bf9.png")
			.headers(image_header)))
		.pause(1)
		.exec(http("ajax-loader.gif")
			.get("/squash/images/ajax-loader-16x16-b8205267b066fdc2c3a4f875daa991bd.gif")
			.headers(image_header))
		.pause(4)
		.exec(http("icons-e987e4a1de2e53deab4d04745dc07a54.png")
			.get("/squash/scripts/ckeditor/skins/moonocolor/icons-e987e4a1de2e53deab4d04745dc07a54.png?t=95e5d83")
			.headers(image_header)
			.resources(http("ui-bg_highlight-soft.png")
			.get("/squash/styles/images/ui-bg_highlight-soft_45_2b2b2b_1x100-09c2e12d9cb77c606f3dcf92f4d08b87.png")
			.headers(image_header),
            http("ui-icons_ffffff.png")
			.get("/squash/styles/images/ui-icons_ffffff_256x240-342bc03f6264c75d3f1d7f99e34295b9.png")
			.headers(image_header),
            http("ui-bg_flat_75.png")
			.get("/squash/styles/images/ui-bg_flat_75_aaaaaa_40x100-2a44fbdb7360c60122bcf6dcef0387d8.png")
			.headers(image_header),
            http("selectFirstProject")
			.get("/squash/requirement-libraries/1")
			.headers(no_content_header),
            http("file-upload-main.js")
			.get("/squash/scripts/file-upload/file-upload-main.js"),
            http("favorite-dashboard-main.js")
			.get("/squash/scripts/favorite-dashboard/favorite-dashboard-main.js"),
            http("rl-management-main.js")
			.get("/squash/scripts/requirement-library-management/rl-management-main.js"),
            http("attachments-bloc.js")
			.get("/squash/scripts/file-upload/attachments-bloc.js"),
            http("favorite-dashboard-view.js")
			.get("/squash/scripts/favorite-dashboard/favorite-dashboard-view.js"),
            http("SimpleFileUploader.js")
			.get("/squash/scripts/file-upload/SimpleFileUploader.js"),
            http("attachments-manager.js")
			.get("/squash/scripts/file-upload/attachments-manager.js"),
            http("requirements-dashboard-main.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/requirements-dashboard-main.js"),
            http("jquery.squash.multi-fileupload.js")
			.get("/squash/scripts/file-upload/jquery.squash.multi-fileupload.js"),
            http("jquery.squash.attachmentsDialog.js")
			.get("/squash/scripts/file-upload/jquery.squash.attachmentsDialog.js"),
            http("model.js")
			.get("/squash/scripts/dashboard/basic-objects/model.js"),
            http("summary.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/summary.js"),
            http("SuperMasterView.js")
			.get("/squash/scripts/dashboard/SuperMasterView.js"),
            http("criticality-pie.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/criticality-pie.js"),
            http("coverage-donut.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/coverage-donut.js"),
            http("bound-description-pie.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/bound-description-pie.js"),
            http("pie-view.js")
			.get("/squash/scripts/dashboard/basic-objects/pie-view.js"),
            http("bound-test-cases-pie.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/bound-test-cases-pie.js"),
            http("donut-view.js")
			.get("/squash/scripts/dashboard/basic-objects/donut-view.js"),
            http("dashboard-utils.js")
			.get("/squash/scripts/dashboard/dashboard-utils.js"),
            http("timestamp-label.js")
			.get("/squash/scripts/dashboard/basic-objects/timestamp-label.js"),
            http("status-pie.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/status-pie.js"),
            http("validation-donut.js")
			.get("/squash/scripts/dashboard/requirements-dashboard/validation-donut.js"),
            http("figleaf-view.js")
			.get("/squash/scripts/dashboard/basic-objects/figleaf-view.js"),
            http("jqplot-view.js")
			.get("/squash/scripts/dashboard/basic-objects/jqplot-view.js"))).exitHereIfFailed
		.pause(28)
		
    val createRequirement = exec(_.set("new_req_count", newReqCount.getAndIncrement))
        .exec(http("createNewRequirement")
			.post("/squash/requirement-browser/drives/1/content/new-requirement")
			.headers(content_json_header)
            .body(StringBody("""{"name":"newReq${new_req_count}","reference":"","description":"","criticality":"MINOR","category":"CAT_TECHNICAL","customFields":{}}""")).asJson
            .check(jsonPath("$.attr.resId").saveAs("new_req_id"))).exitHereIfFailed
        .exec(http("throbber.gif")
			.get("/squash/images/throbber-7b9776076d5fceef4993b55c9383dedd.gif")
			.headers(image_header)
			.resources(http("requirement-browser content")
			.get("/squash/requirement-browser/drives/1/content")
			.headers(accept_json_header),
            http("openNewRequirement")
			.get("/squash/requirements/${new_req_id}")
			.headers(no_content_header),
            http("ui-bg_glass_45.png")
			.get("/squash/styles/jquery/ui/images/ui-bg_glass_45_2b2b2b_1x400-8269426d842770c190b9ef4fbae48bf9.png")
			.headers(image_header),
            http("requirement-version-page.js")
			.get("/squash/scripts/requirement-version-page.js"),
            http("general-information-panel.js")
			.get("/squash/scripts/page-components/general-information-panel.js"),
            http("general-information-panel-controller.js")
			.get("/squash/scripts/page-components/general-information-panel-controller.js"),
            http("entity-milestone-count-notifier.js")
			.get("/squash/scripts/milestones/entity-milestone-count-notifier.js"),
            http("ctxt-handlers-main.js")
			.get("/squash/scripts/contextual-content-handlers/ctxt-handlers-main.js"),
            http("VerifyingTestCasesPanel.js")
			.get("/squash/scripts/verifying-test-cases/VerifyingTestCasesPanel.js"),
            http("squash.wreqr.init.js")
			.get("/squash/scripts/app/squash.wreqr.init.js"),
            http("NameAndReferenceHandler.js")
			.get("/squash/scripts/contextual-content-handlers/NameAndReferenceHandler.js"),
            http("SimpleNameHandler.js")
			.get("/squash/scripts/contextual-content-handlers/SimpleNameHandler.js"),
            http("requirement-coverage-stat-view.js")
			.get("/squash/scripts/req-workspace/requirement-coverage-stat-view.js"),
            http("linked-requirements-panel.js")
			.get("/squash/scripts/req-workspace/linked-requirements-panel.js"),
            http("bugtracker-panel.js")
			.get("/squash/scripts/bugtracker/bugtracker-panel.js"),
            http("ButtonUtil.js")
			.get("/squash/scripts/app/util/ButtonUtil.js"),
            http("jquery.oslc-popup.js")
			.get("/squash/scripts/bugtracker/report-issue-popup/jquery.oslc-popup.js"),
            http("jquery.main-popup.js")
			.get("/squash/scripts/bugtracker/report-issue-popup/jquery.main-popup.js"),
            http("oslc-view-template.html")
			.get("/squash/scripts/bugtracker/report-issue-popup/oslc-view-template.html"),
            http("default-field-view.js")
			.get("/squash/scripts/bugtracker/report-issue-popup/default-field-view.js"),
            http("advanced-field-view.js")
			.get("/squash/scripts/bugtracker/report-issue-popup/advanced-field-view.js"),
            http("BTEntity.js")
			.get("/squash/scripts/bugtracker/domain/BTEntity.js"),
            http("advanced-view-template.html")
			.get("/squash/scripts/bugtracker/report-issue-popup/advanced-view-template.html"),
            http("widget-registry.js")
			.get("/squash/scripts/bugtracker/widgets/widget-registry.js"),
            http("default-view-template.html")
			.get("/squash/scripts/bugtracker/report-issue-popup/default-view-template.html"),
            http("widget.js")
			.get("/squash/scripts/bugtracker/widgets/widget.js"),
            http("text_area.js")
			.get("/squash/scripts/bugtracker/widgets/text_area.js"),
            http("tag_list.js")
			.get("/squash/scripts/bugtracker/widgets/tag_list.js"),
            http("free_tag_list.js")
			.get("/squash/scripts/bugtracker/widgets/free_tag_list.js"),
            http("dropdown_list.js")
			.get("/squash/scripts/bugtracker/widgets/dropdown_list.js"),
            http("date_picker.js")
			.get("/squash/scripts/bugtracker/widgets/date_picker.js"),
            http("checkbox.js")
			.get("/squash/scripts/bugtracker/widgets/checkbox.js"),
            http("text_field.js")
			.get("/squash/scripts/bugtracker/widgets/text_field.js"),
            http("DelegateCommand.js")
			.get("/squash/scripts/bugtracker/domain/DelegateCommand.js"),
            http("file_upload.js")
			.get("/squash/scripts/bugtracker/widgets/file_upload.js"),
            http("timetracker.js")
			.get("/squash/scripts/bugtracker/widgets/timetracker.js"),
            http("date_time.js")
			.get("/squash/scripts/bugtracker/widgets/date_time.js"),
            http("FieldValue.js")
			.get("/squash/scripts/bugtracker/domain/FieldValue.js"),
            http("ui-icons_a8a8a8_256x240-076fc271c49b70310003b64674a3c32c.png")
			.get("/squash/styles/jquery/ui/images/ui-icons_a8a8a8_256x240-076fc271c49b70310003b64674a3c32c.png")
			.headers(image_header),
            http("getAttachments")
			.get("/squash/attach-list/24/attachments/details?sEcho=1&iColumns=5&sColumns=%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=entity-index&bSortable_0=false&mDataProp%5B1%5D=hyphenated-name&bSortable_1=true&mDataProp%5B2%5D=size&bSortable_2=true&mDataProp%5B3%5D=added-on&bSortable_3=true&mDataProp%5B4%5D=empty-delete-holder&bSortable_4=false&iSortCol%5B0%5D=1&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596462850474")
			.headers(accept_json_header),
            http("backToCampaignWorkspaceTree0")
			.get("/squash/campaign-workspace/tree/0")
			.headers(no_content_header),
            http("newReqCoverage")
			.get("/squash/requirement-versions/${new_req_id}/coverage-stats?perimeter=")
			.headers(no_content_header))).exitHereIfFailed

}

    
object User {

    val goToUserList = exec(http("goToAdmin")
			.get("/squash/administration")
			.headers(default_header)
			.check(css("meta[name='_csrf']","content").saveAs("csrf_token"))).exitHereIfFailed
		.pause(4)
		.exec(http("goToUsersList")
			.get("/squash/administration/users/list")
			.headers(default_header)
			.resources(http("getDatatableMessages")
			.get("/squash/datatables/messages")
			.headers(no_content_header),
            http("getUserList")
			.get("/squash/administration/users/table?sEcho=1&iColumns=13&sColumns=%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=user-index&sSearches%5B0%5D=&bRegex_0=false&bSearchable_0=true&bSortable_0=false&mDataProp%5B1%5D=user-active&sSearches%5B1%5D=&bRegex_1=false&bSearchable_1=true&bSortable_1=false&mDataProp%5B2%5D=user-login&sSearches%5B2%5D=&bRegex_2=false&bSearchable_2=true&bSortable_2=true&mDataProp%5B3%5D=user-group&sSearches%5B3%5D=&bRegex_3=false&bSearchable_3=true&bSortable_3=true&mDataProp%5B4%5D=user-firstname&sSearches%5B4%5D=&bRegex_4=false&bSearchable_4=true&bSortable_4=true&mDataProp%5B5%5D=user-lastname&sSearches%5B5%5D=&bRegex_5=false&bSearchable_5=true&bSortable_5=true&mDataProp%5B6%5D=user-email&sSearches%5B6%5D=&bRegex_6=false&bSearchable_6=true&bSortable_6=true&mDataProp%5B7%5D=user-created-on&sSearches%5B7%5D=&bRegex_7=false&bSearchable_7=true&bSortable_7=true&mDataProp%5B8%5D=user-created-by&sSearches%5B8%5D=&bRegex_8=false&bSearchable_8=true&bSortable_8=true&mDataProp%5B9%5D=user-modified-on&sSearches%5B9%5D=&bRegex_9=false&bSearchable_9=true&bSortable_9=true&mDataProp%5B10%5D=user-modified-by&sSearches%5B10%5D=&bRegex_10=false&bSearchable_10=true&bSortable_10=true&mDataProp%5B11%5D=user-connected-on&sSearches%5B11%5D=&bRegex_11=false&bSearchable_11=true&bSortable_11=true&mDataProp%5B12%5D=empty-delete-holder&sSearches%5B12%5D=&bRegex_12=false&bSearchable_12=true&bSortable_12=false&sSearch=&bRegex=false&iSortCol%5B0%5D=2&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596186344366")
			.headers(no_content_header),
            http("getDatatableMessages")
			.get("/squash/datatables/messages")
			.headers(no_content_header))).exitHereIfFailed
		.pause(28)
		
    val createNewUser = exec(_.set("new_user_id", newUserId.getAndIncrement))
		.exec(http("createNewUser")
			.post("/squash/administration/users/new")
			.headers(form_content_header)
			.formParam("login", "newuser${new_user_id}")
			.formParam("firstName", "newuser")
			.formParam("lastName", "newuser")
			.formParam("email", "new@us.er")
			.formParam("groupId", "2")
			.formParam("password", "newuser")
			.resources(http("getUserListPostCreation")
			.get("/squash/administration/users/table?sEcho=2&iColumns=13&sColumns=%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C%2C&iDisplayStart=0&iDisplayLength=50&mDataProp%5B0%5D=user-index&sSearches%5B0%5D=&bRegex_0=false&bSearchable_0=true&bSortable_0=false&mDataProp%5B1%5D=user-active&sSearches%5B1%5D=&bRegex_1=false&bSearchable_1=true&bSortable_1=false&mDataProp%5B2%5D=user-login&sSearches%5B2%5D=&bRegex_2=false&bSearchable_2=true&bSortable_2=true&mDataProp%5B3%5D=user-group&sSearches%5B3%5D=&bRegex_3=false&bSearchable_3=true&bSortable_3=true&mDataProp%5B4%5D=user-firstname&sSearches%5B4%5D=&bRegex_4=false&bSearchable_4=true&bSortable_4=true&mDataProp%5B5%5D=user-lastname&sSearches%5B5%5D=&bRegex_5=false&bSearchable_5=true&bSortable_5=true&mDataProp%5B6%5D=user-email&sSearches%5B6%5D=&bRegex_6=false&bSearchable_6=true&bSortable_6=true&mDataProp%5B7%5D=user-created-on&sSearches%5B7%5D=&bRegex_7=false&bSearchable_7=true&bSortable_7=true&mDataProp%5B8%5D=user-created-by&sSearches%5B8%5D=&bRegex_8=false&bSearchable_8=true&bSortable_8=true&mDataProp%5B9%5D=user-modified-on&sSearches%5B9%5D=&bRegex_9=false&bSearchable_9=true&bSortable_9=true&mDataProp%5B10%5D=user-modified-by&sSearches%5B10%5D=&bRegex_10=false&bSearchable_10=true&bSortable_10=true&mDataProp%5B11%5D=user-connected-on&sSearches%5B11%5D=&bRegex_11=false&bSearchable_11=true&bSortable_11=true&mDataProp%5B12%5D=empty-delete-holder&sSearches%5B12%5D=&bRegex_12=false&bSearchable_12=true&bSortable_12=false&sSearch=&bRegex=false&iSortCol%5B0%5D=2&sSortDir%5B0%5D=asc&iSortingCols=1&_=1596186344367")
			.headers(no_content_header)))

}

	val newReq = scenario("New Requirement").exec(Login.login, Requirement.goToRequirementWorkspace, Requirement.createRequirement)
	
	val newUser = scenario("New User").exec(Login.login, User.goToUserList, User.createNewUser)
	
	val checkTestCase = scenario("Check First Test Case").exec(Login.login, TestCase.goToTestCaseWorkspace,TestCase.selectTestCaseOne)

	setUp(newReq.inject(rampUsers(1000) during (2000 seconds)), newUser.inject(rampUsers(1000) during (2000 seconds)), checkTestCase.inject(rampUsers(18000) during (2000 seconds))).protocols(httpProtocol)
}

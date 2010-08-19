<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<title>OOICI - LCA Demo</title>
	<link rel="stylesheet" type="text/css" href="../../../css/reset.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="../../../css/text.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="../../../css/960.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="../../../css/layout.css" media="screen"/>
	<link rel="stylesheet" type="text/css" href="../../../css/nav.css" media="screen"/>
	<!--[if IE 6]><link rel="stylesheet" type="text/css" href="../../../css/ie6.css" media="screen" /><![endif]-->
	<!--[if IE 7]><link rel="stylesheet" type="text/css" href="../../../css/ie.css" media="screen" /><![endif]-->
</head>
<body>
<div class="container_16">
	<div class="grid_16">
		<h1 id="branding"><a href="../../../">Ocean Observatories Initiative Cyberinfrastructure</a></h1>
	</div>
	<div class="clear"></div>
	<div class="grid_16">
		<ul class="nav main">
			<li><g:link controller="home" action="index">Home</g:link>
			<li><g:link controller="instrument" action="index">Instruments</g:link>
				<ul>
					<li><g:link controller="instrument" action="show">View all Instruments</g:link></li>
					<li><g:link controller="instrument" action="create">Register an Instrument</g:link></li>
				</ul>
			</li>
			<li><g:link controller="dataproduct" action="index">Data Products</g:link>
				<ul>
					<li><g:link controller="dataproduct" action="show">View all Data Products</g:link></li>
					<li><g:link controller="dataproduct" action="create">Register a Data Product</g:link></li>
					<li><g:link controller="instrument" action="runcommand">Command a new Instrument</g:link></li>
				</ul>
			</li>
			<li><a href="">About OOICI</a></li>
			<li><a href="">Contact Us</a></li>
			<li><g:link controller="logout" action="index">Logout</g:link></li>
		</ul>
	</div>
	<div class="clear"></div>
	<div class="grid_16">
		<h2 id="page-heading">LCA Demo</h2>
	</div>
	<div class="clear"></div>
	<div class="grid_4">
		<div class="box menu">
			<h2>Menu</h2>
			<div class="block">
				<ul class="section menu">
					<li><a class="menuitem">Home</a>
					</li>
					<li>Instruments
						<ul class="submenu">
							<li><g:link controller="instrument" action="show">View all Instruments</g:link></li>
							<li><g:link controller="instrument" action="create" class="active">Register an Instrument</g:link></li>
							<li><g:link controller="instrument" action="runcommand">Command a new Instrument</g:link></li>
						</ul>
					</li>
					<li>Data Products
						<ul class="submenu">
							<li><g:link controller="dataproduct" action="show">View all Data Products</g:link></li>
							<li><g:link controller="dataproduct" action="create">Register a Data Product</g:link></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="grid_7">
		<div class="box">
			<h2><a href="#" id="toggle-blockquote">Welcome</a></h2>
			<div class="block" id="blockquote">
				<blockquote>
					<p>To Register a new Instrument, please enter the Name, Manufacturer, Model Number, Serial Number and Firmware version for the new Instrument.  Select the Register button when done.</p>
					<p class="cite"><cite>OOICI Support</cite></p>
				</blockquote>
			</div>
		</div>
		<div class="box">
			<h2><a href="#" id="toggle-forms">Register a new Instrument</a></h2>
			<div class="block" id="forms">
				<g:form action="save" method="post">
					<fieldset>
						<g:if test="${flash.message}">
							<div class="message">${flash.message}</div>
						</g:if>
						<g:hasErrors bean="${instrumentInstance}">
							<div class="errors">
								<g:renderErrors bean="${instrumentInstance}" as="list"/>
							</div>
						</g:hasErrors>
						<legend>New Instrument</legend>
						<p>
							<label for="name"><g:message code="instrument.name.label" default="Name"/></label>
							<g:textField name="name" value="${instrumentInstance?.name}"/>
						</p>
						<p>
							<label for="manufacturer"><g:message code="instrument.manufacturer.label" default="Manufacturer"/></label>
							<g:textField name="manufacturer" value="${instrumentInstance?.manufacturer}"/>
						</p>
						<p>
							<label for="model"><g:message code="instrument.model.label" default="Model Number"/></label>
							<g:textField name="model" value="${instrumentInstance?.model}"/>
						</p>
						<p>
							<label for="name"><g:message code="instrument.serialNum.label" default="Serial Number"/></label>
							<g:textField name="serialNum" value="${instrumentInstance?.serialNum}"/>
						</p>
						<p>
							<label for="fwVersion"><g:message code="instrument.fwVersion.label" default="Firmware Version"/></label>
							<g:textField name="fwVersion" value="${instrumentInstance?.fwVersion}"/>
						</p>
						<g:submitButton name="Register" class="save" value="Register" />
						<g:submitButton name="Cancel" value="Cancel" />
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>
	<div class="grid_5">
	</div>
	<div class="clear"></div>
	<div class="grid_16" id="site_info">
		<div class="box">
			<p>OOICI 2010 - LCA</p>
		</div>
	</div>
	<div class="clear"></div>
</div>
<script type="text/javascript" src="../../../js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="../../../js/jquery-ui.js"></script>
<script type="text/javascript" src="../../../js/jquery-fluid16.js"></script>
</body>
</html>
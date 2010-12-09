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
			<li><a href="http://www.oceanleadership.org/about-ocean-leadership/">About OOICI</a></li>
			<li><g:link controller="logout" action="index">Logout</g:link></li>
		</ul>
	</div>
	<div class="clear"></div>
	<div class="grid_16">
		<h2 id="page-heading">LCA Demo - Command an Instrument</h2>
	</div>
	<div class="clear"></div>
	<div class="grid_4">
		<div class="box menu">
			<h2>Menu</h2>
			<div class="block">
				<ul class="section menu">
					<li>Instruments
						<ul class="submenu">
							<li><g:link controller="instrument" action="show">View all Instruments</g:link></li>
							<li><g:link controller="instrument" action="create">Register an Instrument</g:link></li>
							<li><g:link controller="instrument" action="runcommand" class="active">Command a new Instrument</g:link></li>
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
					<p>To Command an Instrument, in the form below, please enter the Name, Manufacturer, Model Number, Serial Number and Firmware version.  Select the Register button when done.</p>
					<p class="cite"><cite>OOICI Support</cite></p>
				</blockquote>
			</div>
		</div>
		<div class="box">
			<h2><a href="#" id="toggle-forms">Register a new Instrument</a></h2>
			<div class="block" id="forms">
				<g:form action="command" method="post">
					<fieldset>
						<g:if test="${flash.message}">
							<div class="message">${flash.message}</div>
						</g:if>
						<g:hasErrors bean="${instrumentInstance}">
							<div class="errors">
								<g:renderErrors bean="${instrumentInstance}" as="list"/>
							</div>
						</g:hasErrors>
						<legend>Command an Instrument</legend>
						<p>
							<label for="instrumentId">Instrument</label>
							<g:select optionValue="name" optionKey="registryId" name="instrumentId" from="${instruments}" value="${selectedInstrId}" noSelection="['':'-Select an Instrument-']"/>
						</p>
						<p>
							<label for="command"><g:message default="Command" />Command</label>
							<g:select name="command" from="${['start', 'stop', 'dps', 'ds']}" noSelection="['':'-Select a Command-']"/> 
						</p>
						<p>
							<label><g:message default="Argument 1" />Argument 1</label>
							<g:textField name="arg0" value="" />
						</p>
						<p>
							<label><g:message default="Argument 2" />Argument 2</label>
							<g:textField name="arg1" value="" />
						</p>
						<g:submitButton name="SendCommand" class="save" value="Send Command"/>
						<g:submitButton name="Cancel" value="Cancel"/>
					</fieldset>
				</g:form>
			</div>
		</div>
	</div>
	<div class="grid_5">
		<div class="box articles">
			<h2>
				<a href="http://www.oceanleadership.org/category/discovery/" id="toggle-articles">News</a>
			</h2>
			<div class="block" id="articles">
				<div class="first article">
					<h3>
						<a href="http://www.oceanleadership.org/category/discovery/">Discovery</a>
					</h3>
					<h4>Science Report describes deepwater horizon oil plume as of June</h4>
					<p class="meta">Ocean Observing</p>
					<a href="http://www.oceanleadership.org/2010/science-report-describes-deepwater-horizon-oil-plume-as-of-june/" class="image">
						<img src="http://www.oceanleadership.org/wp-content/themes/arthemia-premium/scripts/timthumb.php?src=http://www.oceanleadership.org/wp-content/uploads/2010/08/0819sp_gulf_oil_burn_350w-250x244.jpg&w=80&h=80&zc=1&q=100" width="60" height="60" alt="photo"/>
					</a>
					<p>In mid-June, two months after the 20 April blowout of BP’s Deepwater Horizon drilling rig, the resulting oil plume had reached huge proportions, peer-reviewed research published 19 August on the Science Express Web site suggests.<a href="http://www.oceanleadership.org/category/discovery/"> More articles.</a></p>
				</div>
			</div>
		</div>
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
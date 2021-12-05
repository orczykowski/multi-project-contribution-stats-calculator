<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Multi project contribution report - boringstuff.pl</title>
</head>
<style>
    :root {
        --menuWidth: 21%;
        --reportWidth: calc(100% - var(--menuWidth))
    }

    body {
        all: unset;
        font-family: "Arial", serif;
        background-color: #2c2c2c;
        color: #a2a2a2;
    }

    nav {
        width: var(--menuWidth);
        text-align: center;
        position: fixed;
        background-color: #3c3f41;
        height: 100%;
        -webkit-box-shadow: inset 14px 0 44px -13px rgba(0, 0, 0, 0.75);
        -moz-box-shadow: inset 14px 0 44px -13px rgba(0, 0, 0, 0.75);
        box-shadow: inset 14px 0 44px -13px rgba(0, 0, 0, 0.75);
    }

    nav h1 {
        padding-bottom: 20px;
        color: #ba9a53;
    }

    nav hr {
        border-bottom: solid 1px #a2a2a2;
        width: 90%;
    }

    nav ol {
        margin: 0;
        padding: 0;
        vertical-align: middle;
    }

    nav ol li {
        list-style-type: none;
    }

    nav ol li a {
        font-size: 14px;
        display: block;
        width: 100%;
        padding: 12px;
        height: 2em;
    }

    a, a:visited {
        color: #fff;
        text-decoration: none;
    }

    nav ol li:hover {
        background-color: #b8acac;
        border-left: #3e829b 7px solid;
    }

    nav ol li:hover a {
        color: #121212;
    }

    article {
        padding: 7px;
    }

    #reports {
        clear: left;
        position: absolute;
        display: block;
        border-left: #323436 solid 10px;
        width: var(--reportWidth);
        margin-left: var(--menuWidth);
    }

    #reports article {
        width: 97%;
        margin: auto;
    }

    #reports article h2 {
        color: #cbcccf;
    }

    .label {
        font-style: italic;
        display: inline-block;
        width: 12em;
    }

    .mainInfo {
        font-weight: bold;
        color: #3e829b;
    }

    table {
        overflow: hidden;
        width: 100%;
        margin: 2em auto;
        padding: 0 0 0 0;
        border: 1px solid #565656;
    }

    .leftAlignColumn {
        text-align: left;
    }

    table td, table th {
        padding: 1em 0.5em;
        text-align: center;
    }

    table tr:nth-child(odd) {
        background-color: #2f2f2f;
    }

    table tr:nth-child(even) {
        background-color: #313131;
    }

    table th {
        background-color: #454545;
    }

    tr:hover td {
        background-color: #055f9f;
        color: #fef;
    }

    tr.projectName th {
        background: none;
        width: 100%;

    }

    .aggregateRow td {
        font-weight: bold;
        color: #fef;
    }

    #boringstuff {
        position: absolute;
        bottom: 0.1em;
        left: 20%;
    }

    #boringstuff a:hover {
        color: #3e829b;
    }

    .errors {
        border: 1px solid #85514c;
        padding: 1em 0 1em 2em;
        border-radius: 5px;
        background-color: #3c3f41;
    }

    .errors li {
        color: #994c41;
        padding: 5px;
    }
</style>
<body>
<nav>
    <h1>Contribution report</h1>
    <hr/>
    <ol>
        <li><a href="#generalInformation">GENERAL INFORMATION</a></li>
        <li><a href="#totalContribution">TOTAL CONTRIBUTION FOR ALL REPOSITORIES</a></li>
        <li><a href="#userContribution">USER CONTRIBUTION RANKING</a></li>
        <li><a href="#repositoryStats">PROJECT CONTRIBUTION STATISTICS</a></li>
    </ol>
    <hr/>
    <p id="boringstuff">created by <a href="http://boringstuff.pl">boringstuff.pl</a></p>
</nav>
<div id="reports">
    <#assign failuresCount=report.failures()?size >
    <#if failuresCount gt 0>
        <article class="errors">
            <h2>Errors</h2>
            <p>errors that occurred while computing the report</p>
            <ul>
                <#list report.failures() as error>
                    <li>${error}</li>
                </#list>
            </ul>
        </article>
    </#if>
    <article id="generalInformation">
        <h2>General information</h2>
        <p>General information on projects for which statistics are calculated and the period for which
            the work was
            taken into stats</p>
        <hr/>
        <p><span class="label">calculation date:</span><span
                    class="mainInfo">${report.calculationDate()}</span></p>
        <p><span class="label">start of the calculation period:</span><span
                    class="mainInfo">${report.dateFrom()}</span>
        </p>
        <table>
            <thead>
            <tr>
                <th>PROJECT NAME</th>
                <th>REPOSITORY URL</th>
                <th>EXCLUDED PATHS</th>
            </tr>
            </thead>
            <#list report.projectContributionStats() as projectStats>
                <tr>
                    <td class="leftAlignColumn">${projectStats.project().getName()}</td>
                    <td class="leftAlignColumn">${projectStats.project().getRepositoryUrl()}</td>
                    <td class="leftAlignColumn">
                        <#list projectStats.project().getExcludePaths() as path>${path},</#list>
                    </td>
                </tr>
            </#list>
        </table>
    </article>
    <article id="totalContribution">
        <h2>Total contribution</h2>
        <p>Aggregate statistics for all users by project for given period</p>
        <hr/>
        <table>
            <thead>
            <tr>
                <th>PROJECT NAME</th>
                <th>LINES OF CODE</th>
                <th>COMMITS</th>
                <th>FILES</th>
            </tr>
            </thead>
            <#list report.projectContributionStats() as projectStats>
                <tr>
                    <td class="leftAlignColumn">${projectStats.project().getName()}</td>
                    <td>${projectStats.total().lineOfCode()}</td>
                    <td>${projectStats.total().commits()}</td>
                    <td>${projectStats.total().files()}</td>
                </tr>
            </#list>
            <tr class="aggregateRow">
                <td>Sum for all projects</td>
                <td>${report.totalContribution().lineOfCode()}</td>
                <td>${report.totalContribution().commits()}</td>
                <td>${report.totalContribution().files()}</td>
            <tr/>
        </table>
    </article>
    <article id="userContribution">
        <h2>User contribution</h2>
        <p>Detailed statistics per user for all projects for given period</p>
        <hr/>
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>USER NAME</th>
                <th>LINES OF CODE</th>
                <th>COMMITS</th>
                <th>FILES</th>
            </tr>
            </thead>
            <#assign i=1>
            <#list report.totalUserContributionStats() as userStats >
                <tr>
                    <td>${i}</td>
                    <td>${userStats.user().name()}</td>
                    <td>${userStats.counts().lineOfCode()}</td>
                    <td>${userStats.counts().commits()}</td>
                    <td>${userStats.counts().files()}</td>
                    <#assign i=i+1>
                </tr>
            </#list>
        </table>
    </article>
    <article id="repositoryStats">
        <h2>Project contribution</h2>
        <p>Detailed statistics on contributions to projects by user</p>
        <hr/>
        <#list report.projectContributionStats() as projectStats>
            <table>
                <thead>
                <tr class="projectName">
                    <th colspan="8">${projectStats.project().getName()}</th>
                </tr>
                <tr>
                    <th>#</th>
                    <th>USER NAME</th>
                    <th>LINES OF CODE</th>
                    <th>COMMITS</th>
                    <th>FILES</th>
                    <th>% of lines</th>
                    <th>% of commits</th>
                    <th>% of files</th>
                </tr>
                </thead>
                <#assign i=1>
                <#list projectStats.userStats() as userStats >
                    <tr>
                        <td>${i}</td>
                        <td>${userStats.user().name()}</td>
                        <td>${userStats.counts().lineOfCode()}</td>
                        <td>${userStats.counts().commits()}</td>
                        <td>${userStats.counts().files()}</td>
                        <td>${userStats.distribution().codeLinesParticipation()}</td>
                        <td>${userStats.distribution().commitsParticipation()}</td>
                        <td>${userStats.distribution().filesParticipation()}</td>
                    </tr>
                    <#assign i=i+1>
                </#list>
            </table>
        </#list>
    </article>
</div>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Multi project contribution report - orczykowski.pl</title>
</head>
<style>
    :root {
        --menuWidth: 21%;
        --reportWidth: calc(100% - var(--menuWidth));
        --bg-primary: #1e1e22;
        --bg-secondary: #26262b;
        --bg-nav: #2a2a30;
        --bg-table-odd: #242428;
        --bg-table-even: #2a2a2f;
        --bg-table-header: #32323a;
        --text-primary: #d4d4d8;
        --text-secondary: #9e9ea6;
        --text-heading: #e8e8ec;
        --accent: #4a9ebb;
        --accent-gold: #c9a84c;
        --border: #3a3a42;
        --border-light: #44444e;
        --hover-bg: #1a5a80;
    }

    body {
        all: unset;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        background-color: var(--bg-primary);
        color: var(--text-primary);
        line-height: 1.5;
        font-size: 14px;
    }

    nav {
        width: var(--menuWidth);
        text-align: center;
        position: fixed;
        background-color: var(--bg-nav);
        height: 100%;
        border-right: 1px solid var(--border);
        box-shadow: 2px 0 12px rgba(0, 0, 0, 0.3);
        z-index: 10;
    }

    nav h1 {
        padding: 24px 16px 20px;
        margin: 0;
        color: var(--accent-gold);
        font-size: 18px;
        font-weight: 600;
        letter-spacing: 0.3px;
    }

    nav hr {
        border: none;
        border-bottom: 1px solid var(--border);
        width: 85%;
        margin: 8px auto;
    }

    nav ol {
        margin: 8px 0;
        padding: 0;
        vertical-align: middle;
    }

    nav ol li {
        list-style-type: none;
    }

    nav ol li a {
        font-size: 12px;
        font-weight: 500;
        letter-spacing: 0.3px;
        display: block;
        width: 100%;
        padding: 12px 16px;
        height: auto;
        color: var(--text-secondary);
        transition: all 0.15s ease;
        text-align: left;
        box-sizing: border-box;
    }

    a, a:visited {
        color: var(--text-primary);
        text-decoration: none;
    }

    nav ol li:hover {
        background-color: rgba(74, 158, 187, 0.1);
        border-left: 3px solid var(--accent);
    }

    nav ol li:hover a {
        color: #fff;
        padding-left: 13px;
    }

    article {
        padding: 12px 0;
    }

    #reports {
        clear: left;
        position: absolute;
        display: block;
        width: var(--reportWidth);
        margin-left: var(--menuWidth);
    }

    #reports article {
        width: 93%;
        margin: 0 auto;
        padding: 24px 0;
    }

    #reports article h2 {
        color: var(--text-heading);
        font-size: 20px;
        font-weight: 600;
        margin-bottom: 6px;
    }

    #reports article p {
        color: var(--text-secondary);
        font-size: 13px;
        margin-top: 2px;
        margin-bottom: 12px;
    }

    #reports article hr {
        border: none;
        border-bottom: 1px solid var(--border);
        margin: 16px 0;
    }

    .label {
        font-style: normal;
        font-size: 13px;
        color: var(--text-secondary);
        display: inline-block;
        width: 14em;
    }

    .mainInfo {
        font-weight: 600;
        color: var(--accent);
        font-size: 14px;
    }

    table {
        overflow: hidden;
        width: 100%;
        margin: 16px auto 24px;
        padding: 0;
        border: 1px solid var(--border);
        border-radius: 6px;
        border-collapse: separate;
        border-spacing: 0;
    }

    .leftAlignColumn {
        text-align: left;
    }

    table td, table th {
        padding: 10px 12px;
        text-align: center;
        font-size: 13px;
        border-bottom: 1px solid var(--border);
    }

    table tr:last-child td {
        border-bottom: none;
    }

    table tr:nth-child(odd) {
        background-color: var(--bg-table-odd);
    }

    table tr:nth-child(even) {
        background-color: var(--bg-table-even);
    }

    table th {
        background-color: var(--bg-table-header);
        color: var(--text-heading);
        font-weight: 600;
        font-size: 11px;
        letter-spacing: 0.5px;
        text-transform: uppercase;
        padding: 12px;
        border-bottom: 2px solid var(--accent);
    }

    tr:hover td {
        background-color: var(--hover-bg);
        color: #fff;
    }

    tr.projectName th {
        background: var(--bg-secondary);
        width: 100%;
        font-size: 14px;
        letter-spacing: 0.2px;
        text-transform: none;
        color: var(--accent-gold);
        border-bottom: 1px solid var(--border);
        padding: 14px 12px;
    }

    .aggregateRow td {
        font-weight: 600;
        color: #fff;
        background-color: rgba(74, 158, 187, 0.12);
        border-top: 2px solid var(--accent);
    }

    #orczykowski {
        position: absolute;
        bottom: 16px;
        left: 0;
        width: 100%;
        text-align: center;
        font-size: 12px;
        color: var(--text-secondary);
    }

    #orczykowski a {
        color: var(--text-secondary);
        transition: color 0.15s ease;
    }

    #orczykowski a:hover {
        color: var(--accent);
    }

    .errors {
        border: 1px solid #6b3a35;
        padding: 16px 24px;
        border-radius: 6px;
        background-color: rgba(153, 76, 65, 0.1);
        margin: 16px 0;
    }

    .errors h2 {
        color: #e07060;
    }

    .errors li {
        color: #d4706a;
        padding: 6px 0;
        font-size: 13px;
    }

    .search-box {
        padding: 8px 16px 12px;
    }

    .search-box input {
        width: 85%;
        padding: 9px 12px;
        border: 1px solid var(--border);
        border-radius: 5px;
        background-color: var(--bg-primary);
        color: var(--text-primary);
        font-size: 13px;
        outline: none;
        transition: border-color 0.15s ease;
    }

    .search-box input:focus {
        border-color: var(--accent);
        box-shadow: 0 0 0 2px rgba(74, 158, 187, 0.15);
    }

    .search-box input::placeholder {
        color: #606068;
    }

    tr.hidden-row {
        display: none;
    }
</style>
<body>
<nav>
    <h1>Contribution report</h1>
    <div class="search-box">
        <input type="text" id="userSearch" placeholder="Search by user name..." oninput="filterByUser(this.value)"/>
    </div>
    <hr/>
    <ol>
        <li><a href="#generalInformation">GENERAL INFORMATION</a></li>
        <li><a href="#totalContribution">TOTAL CONTRIBUTION FOR ALL REPOSITORIES</a></li>
        <li><a href="#userContribution">USER CONTRIBUTION RANKING</a></li>
        <li><a href="#repositoryStats">PROJECT CONTRIBUTION STATISTICS</a></li>
    </ol>
    <hr/>
    <p id="orczykowski">created by <a href="https://orczykowski.github.io/aboutme">Orczykowski</a></p>
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
                <th>COMMITS</th>
                <th>LINES ADDED</th>
                <th>LINES REMOVED</th>
                <th>FILES CHANGED</th>
                <th>NEW FILES</th>
                <th>PROD LINES</th>
                <th>TEST LINES</th>
                <th>CONFIG LINES</th>
            </tr>
            </thead>
            <#list report.projectContributionStats() as projectStats>
                <tr>
                    <td class="leftAlignColumn">${projectStats.project().getName()}</td>
                    <td>${projectStats.total().commits()}</td>
                    <td>${projectStats.total().linesAdded()}</td>
                    <td>${projectStats.total().linesRemoved()}</td>
                    <td>${projectStats.total().filesChanged()}</td>
                    <td>${projectStats.total().newFiles()}</td>
                    <td>${projectStats.total().productionLinesAdded()}</td>
                    <td>${projectStats.total().testLinesAdded()}</td>
                    <td>${projectStats.total().configLinesAdded()}</td>
                </tr>
            </#list>
            <tr class="aggregateRow">
                <td>Sum for all projects</td>
                <td>${report.totalContribution().commits()}</td>
                <td>${report.totalContribution().linesAdded()}</td>
                <td>${report.totalContribution().linesRemoved()}</td>
                <td>${report.totalContribution().filesChanged()}</td>
                <td>${report.totalContribution().newFiles()}</td>
                <td>${report.totalContribution().productionLinesAdded()}</td>
                <td>${report.totalContribution().testLinesAdded()}</td>
                <td>${report.totalContribution().configLinesAdded()}</td>
            <tr/>
        </table>
    </article>
    <article id="userContribution">
        <h2>User contribution</h2>
        <p>Detailed statistics per user for all projects for given period</p>
        <hr/>
        <table class="user-filterable">
            <thead>
            <tr>
                <th>#</th>
                <th>USER NAME</th>
                <th>COMMITS</th>
                <th>LINES ADDED</th>
                <th>LINES REMOVED</th>
                <th>FILES CHANGED</th>
                <th>NEW FILES</th>
                <th>PROD LINES</th>
                <th>TEST LINES</th>
                <th>CONFIG LINES</th>
            </tr>
            </thead>
            <#assign i=1>
            <#list report.totalUserContributionStats() as userStats >
                <tr data-username="${userStats.user().name()}">
                    <td>${i}</td>
                    <td>${userStats.user().name()}</td>
                    <td>${userStats.counts().commits()}</td>
                    <td>${userStats.counts().linesAdded()}</td>
                    <td>${userStats.counts().linesRemoved()}</td>
                    <td>${userStats.counts().filesChanged()}</td>
                    <td>${userStats.counts().newFiles()}</td>
                    <td>${userStats.counts().productionLinesAdded()}</td>
                    <td>${userStats.counts().testLinesAdded()}</td>
                    <td>${userStats.counts().configLinesAdded()}</td>
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
            <table class="user-filterable">
                <thead>
                <tr class="projectName">
                    <th colspan="13">${projectStats.project().getName()}</th>
                </tr>
                <tr>
                    <th>#</th>
                    <th>USER NAME</th>
                    <th>COMMITS</th>
                    <th>LINES ADDED</th>
                    <th>LINES REMOVED</th>
                    <th>FILES CHANGED</th>
                    <th>NEW FILES</th>
                    <th>PROD LINES</th>
                    <th>TEST LINES</th>
                    <th>CONFIG LINES</th>
                    <th>% of commits</th>
                    <th>% of lines added</th>
                    <th>% of lines removed</th>
                </tr>
                </thead>
                <#assign i=1>
                <#list projectStats.userStats() as userStats >
                    <tr data-username="${userStats.user().name()}">
                        <td>${i}</td>
                        <td>${userStats.user().name()}</td>
                        <td>${userStats.counts().commits()}</td>
                        <td>${userStats.counts().linesAdded()}</td>
                        <td>${userStats.counts().linesRemoved()}</td>
                        <td>${userStats.counts().filesChanged()}</td>
                        <td>${userStats.counts().newFiles()}</td>
                        <td>${userStats.counts().productionLinesAdded()}</td>
                        <td>${userStats.counts().testLinesAdded()}</td>
                        <td>${userStats.counts().configLinesAdded()}</td>
                        <td>${userStats.distribution().commitsParticipation()}</td>
                        <td>${userStats.distribution().linesAddedParticipation()}</td>
                        <td>${userStats.distribution().linesRemovedParticipation()}</td>
                    </tr>
                    <#assign i=i+1>
                </#list>
            </table>
        </#list>
    </article>
</div>
<script>
    function filterByUser(query) {
        var search = query.toLowerCase().trim();
        var tables = document.querySelectorAll('table.user-filterable');
        tables.forEach(function(table) {
            var rows = table.querySelectorAll('tr[data-username]');
            rows.forEach(function(row) {
                var username = row.getAttribute('data-username');
                if (search === '' || username.indexOf(search) !== -1) {
                    row.classList.remove('hidden-row');
                } else {
                    row.classList.add('hidden-row');
                }
            });
        });
    }
</script>
</body>
</html>

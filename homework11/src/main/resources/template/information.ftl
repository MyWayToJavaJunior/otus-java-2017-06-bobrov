<#-- @ftlvariable name="hit" type="java.lang.Number" -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Information</title>
</head>
<body>
<div>
    <button class="reload">Reload</button>
</div>
<div>
    Hit: <span class="hit">-</span>
    <br>
    Miss: <span class="miss">-</span>
    <br>
    Size: <span class="size">-</span>
    <br>
    Max size: <span class="max-size">-</span>
    <br>
    Idle Time: <span class="idle-time">-</span>
    <br>
    Life Time: <span class="life-time">-</span>
    <br>
    Is eternal: <span class="is-eternal">-</span>
</div>
</body>
<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<script type="application/javascript">

    window.onload = (function () {
        reload();
        $(".reload").click(reload);
    })();

    function reload() {
        $.ajax({
            url: "/stat"
        }).done(function (data) {
            $(".hit").html(data.hit);
            $(".miss").html(data.miss);
            $(".size").html(data.size);
            $(".max-size").html(data.maxSize);
            $(".idle-time").html(data.idleTime);
            $(".life-time").html(data.lifeTime);
            $(".is-eternal").html(data.isEternal.toString());
        });
    }
</script>
</html>
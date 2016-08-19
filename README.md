# excellent-ball

Pure java の adb接続ライブラリ(https://github.com/cgutman/AdbLib )を使って、自分自身にadb接続し、
/system/bin/input swipe
を送るアプリです。

画面上のボタンか、Project Linking(https://linkingiot.com/ )の対応デバイス(https://ssl.braveridge.com/store/html/products/detail.php?product_id=30 )を使って、デバイスのボタンを押すことで
モンスターボールを3種類(近、中、遠)の飛距離で投げることができます。

必ずExcellentが取れるわけではありませんが、若干確率が高い気がします。

端末の処理遅延などによって、飛距離が出ない場合もあります。

アプリを使用する前に
開発者モードをON、デバッグをONにして、端末とUSB接続し、
adb tcpip 5555
とコマンドを打って、adbをtcpipモードにしておく必要があります。

・参考
物理ボタンを押してモンスターボールを投げる方法
http://qiita.com/kojira/items/ee81043bd9ad7c41ad4f
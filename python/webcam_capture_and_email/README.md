Basic usage:
```
python webcam_capture_and_email.py --toaddr="recipient@gmail.com" --username="sender@gmail.com" --password="topsecret" --subject="webcam test"
```

Supported capture sources:

* fswebcam      # Linux option 1
* pygame        # Linux option 2 (PyGame + OpenCV)
* videocapture  # Windows option <http://videocapture.sourceforge.net/>

Installing fswebcam:
```
sudo apt install fswebcam
fswebcam -d /dev/video0 -r 640x480 test.jpg
xdg-open test.jpg
```

List webcam resolutions
```
sudo apt install uvcdynctrl
uvcdynctrl -l
```
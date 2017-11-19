import socket
import sys

sys.path.insert(0, '/home/pi/Desktop/raspi/src/python')

from neopixel import *
import time

# LED strip configuration:

LED_COUNT = 7  # Number of LED pixels.
LED_PIN = 18  # GPIO pin connected to the pixels (18 uses PWM!).
LED_FREQ_HZ = 800000  # LED signal frequency in hertz (usually 800khz)
LED_DMA = 5  # DMA channel to use for generating signal (try 5)
LED_BRIGHTNESS = 150  # Set to 0 for darkest and 255 for brightest
LED_INVERT = False  # True to invert the signal (when using NPN transistor level shift)
LED_CHANNEL = 0  # set to '1' for GPIOs 13, 19, 41, 45 or 53
LED_STRIP = ws.WS2811_STRIP_GRB  # Strip type and colour ordering

# Create NeoPixel object with appropriate configuration.
strip = Adafruit_NeoPixel(LED_COUNT, LED_PIN, LED_FREQ_HZ, LED_DMA, LED_INVERT, LED_BRIGHTNESS, LED_CHANNEL, LED_STRIP)


previous_state='color:255,255,255'
current_state='color:255,255,255'


# Define functions which animate LEDs in various ways.
def colorWipe(color, wait_ms=50):
    """Wipe color across display a pixel at a time."""
    for i in range(strip.numPixels()):
        strip.setPixelColor(i, color)
        strip.show()
    time.sleep(wait_ms / 1000.0)


def leds_on(color):
    for i in range(strip.numPixels()):
        strip.setPixelColor(i, color)
    strip.show()


def leds_off():
    for i in range(strip.numPixels()):
        strip.setPixelColor(i, Color(0, 0, 0))
    strip.show()


def wheel(pos):
    """Generate rainbow colors across 0-255 positions."""
    if pos < 85:
        return Color(pos * 3, 255 - pos * 3, 0)
    elif pos < 170:
        pos -= 85
        return Color(255 - pos * 3, 0, pos * 3)
    else:
        pos -= 170
        return Color(0, pos * 3, 255 - pos * 3)


def rainbow(wait_ms=20, iterations=1):
    """Draw rainbow that fades across all pixels at once."""
    for j in range(256 * iterations):
        for i in range(strip.numPixels()):
            strip.setPixelColor(i, wheel((i + j) & 255))
        strip.show()
        time.sleep(wait_ms / 1000.0)


def rainbowCycle(wait_ms=20, iterations=5):
    """Draw rainbow that uniformly distributes itself across all pixels."""
    for j in range(256 * iterations):
        for i in range(strip.numPixels()):
            strip.setPixelColor(i, wheel((int(i * 256 / strip.numPixels()) + j) & 255))
        strip.show()
        time.sleep(wait_ms / 1000.0)


def handle(data):
    global previous_state
    global current_state
    
    if data == "prev" and previous_state != 'prev':
        print('previous state', previous_state)
        handle(previous_state)
        return
    
    previous_state = current_state
    current_state = data
    
    print('data received', data)
    if data == "on":
        leds_on(Color(255, 255, 255))
    if data == "off":
        leds_off()
    if data.startswith("color:") and len(data) == 17:  # color:255,255,255
        leds_on(Color(int(data[6:9]), int(data[10:13]), int(data[14:17])))
    if data == "rainbow":
        rainbow()
    if data == "rainbowcycle":
        rainbowCycle()


if __name__ == "__main__":

    TCP_IP = '131.159.212.8'
    TCP_PORT = 4445

    BUFFER_SIZE = 20  # Normally 1024, but we want fast response

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    s.bind((TCP_IP, TCP_PORT))

    s.listen(1)
    
    
    # Intialize the library (must be called once before other functions).
    strip.begin()
    leds_on(Color(255, 0, 0))

    print('Waiting for Connection')

    while 1:
        conn, addr = s.accept()
        print('Connection address:', addr)
        data = conn.recv(BUFFER_SIZE)
        if not data: break
        handle(data)
        conn.close()

    

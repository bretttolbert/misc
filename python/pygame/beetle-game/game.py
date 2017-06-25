#!/usr/bin/env python
import random
import sys

import pygame
from pygame.locals import *


SCREEN_SIZE = (500, 500)

pygame.init()

screen = pygame.display.set_mode(SCREEN_SIZE, 0, 32)
pygame.display.set_caption('Beetles')

background= pygame.image.load('sand.png').convert()
chopsticks_open = pygame.image.load('chopsticks_open.png').convert_alpha()
chopsticks_closed = pygame.image.load('chopsticks_closed.png').convert_alpha()
cursor = chopsticks_open

pygame.mouse.set_visible(False)

blue_bug_up = pygame.image.load('beetle_blue.png').convert_alpha()
blue_bug_down = pygame.transform.flip(blue_bug_up, False, True)
blue_bug_left = pygame.transform.rotate(blue_bug_up, 90)
blue_bug_right = pygame.transform.rotate(blue_bug_up, -90)

purple_bug_up = pygame.image.load('beetle_purple.png').convert_alpha()
purple_bug_down = pygame.transform.flip(purple_bug_up, False, True)
purple_bug_left = pygame.transform.rotate(purple_bug_up, 90)
purple_bug_right = pygame.transform.rotate(purple_bug_up, -90)

class Bug:
    
    def __init__(self):
        self.speed = random.random()
        self.image = blue_bug_up

        #determine which direction bug will travel
        if random.randint(0,1) == 0:
            self.type = 'l2r'
            self.position = [0, random.randint(0, SCREEN_SIZE[1])]
        else:
            self.type = 't2b'
            self.position = [random.randint(0, SCREEN_SIZE[0]), 0]

        #pick bug color
        if random.randint(0,1) == 0:
            self.color = 'purple'
        else:
            self.color = 'blue'
            
    def update(self):

        if self.type == 'l2r':
            self.position[0] += self.speed

            #check bug within bounds
            if self.position[0] + self.image.get_width() > SCREEN_SIZE[0]:
                self.speed -= 0.1
            elif self.position[0] < 0:
                self.speed += 0.1

            #make bug image face correct direction
            if self.speed < 0:
                if self.color == 'blue':
                    self.image = blue_bug_left
                else:
                    self.image = purple_bug_left
            else:
                if self.color == 'blue':
                    self.image = blue_bug_right
                else:
                    self.image = purple_bug_right

        else:
            self.position[1] += self.speed

            #check bug within bounds
            if self.position[1] + self.image.get_height() > SCREEN_SIZE[0]:
                self.speed -= 0.1
            elif self.position[1] < 0:
                self.speed += 0.1

            #make bug image face correct direction
            if self.speed < 0:
                self.image = blue_bug_up
            else:
                self.image = blue_bug_down

        screen.blit(self.image, self.position)

bugs = []
for i in range(1):
    bugs.append(Bug())

while True:
    for event in pygame.event.get():
        if event.type == QUIT:
            exit()
        elif event.type == MOUSEBUTTONDOWN:
            cursor = chopsticks_closed
            bugs.append(Bug())
        elif event.type == MOUSEBUTTONUP:
            cursor = chopsticks_open

    screen.blit(background, (0,0))


    for i in bugs:
        i.update()

    x, y = pygame.mouse.get_pos()
    y -= cursor.get_height()
    screen.blit(cursor, (x, y))
    pygame.display.update()

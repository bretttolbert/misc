#!/usr/bin/env python
import random
import sys

import pygame
from pygame.locals import *

DEBUG = True
SCREEN_SIZE = (500, 500)
SCREENX, SCREENY = SCREEN_SIZE

pygame.init()

screen = pygame.display.set_mode(SCREEN_SIZE, 0, 32)
pygame.display.set_caption('Beetles')

background= pygame.image.load('desert.jpg').convert()
background = pygame.transform.scale(background, SCREEN_SIZE)
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

class Pos:

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def toarray(self):
        return [self.x, self.y] 

class Bug:
    
    def __init__(self):
        self.speed = random.random()
        self.image = blue_bug_up

        #determine which direction bug will travel
        if random.randint(0,1) == 0:
            self.type = 'l2r'
            self.pos = Pos(0, random.randint(0, SCREEN_SIZE[1]))
        else:
            self.type = 't2b'
            self.pos = Pos(random.randint(0, SCREEN_SIZE[0]), 0)

        #pick bug color
        if random.randint(0,1) == 0:
            self.color = 'purple'
        else:
            self.color = 'blue'
            

    def correct_direction(self):
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


    def update(self):

        if self.type == 'l2r':
            self.pos.x += self.speed

            #check bug within bounds
            if self.pos.x + self.image.get_width() > SCREEN_SIZE[0]:
                self.speed -= 0.1
            elif self.pos.x < 0:
                self.speed += 0.1

            self.correct_direction()
        else:
            self.pos.y += self.speed

            #check bug within bounds
            if self.pos.y + self.image.get_height() > SCREEN_SIZE[0]:
                self.speed -= 0.1
            elif self.pos.y < 0:
                self.speed += 0.1

            #make bug image face correct direction
            if self.speed < 0:
                self.image = blue_bug_up
            else:
                self.image = blue_bug_down

        screen.blit(self.image, self.pos.toarray())

    def pinch_matches(self, pinch_pos):
        pinchx, pinchy = pinch_pos[0], pinch_pos[1]
        return (pinchx >= self.pos.x and pinchx <= self.pos.x + self.image.get_width()
            and pinchy >= self.pos.y and pinchy <= self.pos.y + self.image.get_height())


bugs = []
for i in range(1):
    bugs.append(Bug())

def pinch_matches_any_bug(pinch_pos):
    global bugs
    pinchx, pinchy = pinch_pos[0], pinch_pos[1]
    if DEBUG:
        pygame.draw.rect(screen,(255,0,0),(pinchx-6,pinchy-6,12,12))
    for bug in bugs:
        if bug.pinch_matches(pinch_pos):
            bugs = [b for b in bugs if b != bug]
            return True
    return False

while True:
    screen.blit(background, (0,0))
    for event in pygame.event.get():
        if event.type == QUIT:
            exit()
        elif event.type == MOUSEBUTTONDOWN:
            cursor = chopsticks_closed
            if pinch_matches_any_bug(event.pos):
                bug_spawn_rate = 2
                for i in range(bug_spawn_rate):
                    bugs.append(Bug())
            else:
                print('miss')
        elif event.type == MOUSEBUTTONUP:
            cursor = chopsticks_open

    for i in bugs:
        i.update()

    x, y = pygame.mouse.get_pos()
    y -= cursor.get_height()
    screen.blit(cursor, (x, y))
    pygame.display.update()

#!/usr/bin/env python
import random
import sys

import pygame
from pygame.locals import *


SCREEN_SIZE = (500, 500)

class DirtMatrix:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.w = 30
        self.h = 10
        self.elem_px = 10
        self.M = [[1 for x in range(self.w)] for y in range(self.h)]

    def draw(self, screen):
        for y, row in enumerate(self.M):
            for x, elem in enumerate(row):
                if elem == 1:
                    color_saddle_brown = (139, 69, 19)
                    elem_x = x * self.elem_px
                    elem_y = y * self.elem_px
                    pygame.draw.rect(screen, color_saddle_brown, 
                        (elem_x, elem_y, self.elem_px, self.elem_px), 0)

    def update(self, mouse_pos):
        if self.mouse_in_region(mouse_pos):
            for y, row in enumerate(self.M):
                for x, elem in enumerate(row):
                    self.M[y][x] = 0

    def mouse_in_region(self, mouse_pos):
        return (mouse_pos.x >= self.x
            and mouse_pos.x <= self.x * self.w * self.elem_px
            and mouse_pos.y >= self.y
            and mouse_pos.y <= self.y * self.h * self.elem_px)


class MousePos:
    def __init__(self):
        self.x, self.y = pygame.mouse.get_pos()

class DirtGame:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode(SCREEN_SIZE, 0, 32)
        pygame.display.set_caption('DirtGame')
        self.background= pygame.image.load('sand.png').convert()
        self.img_chopsticks_open = \
            pygame.image.load('chopsticks_open.png').convert_alpha()
        self.img_chopsticks_closed = \
            pygame.image.load('chopsticks_closed.png').convert_alpha()
        self.cursor = self.img_chopsticks_open
        pygame.mouse.set_visible(False)
        self.dm = DirtMatrix(10, 10)
        self.mouse_pos = MousePos()

    def run(self):
        while True:
            for event in pygame.event.get():
                if event.type == QUIT:
                    exit()
                elif event.type == MOUSEBUTTONDOWN:
                    self.cursor = self.img_chopsticks_closed
                elif event.type == MOUSEBUTTONUP:
                    self.cursor = self.img_chopsticks_open
            self.update()
            self.draw()

    def update(self):
            self.mouse_pos = MousePos()
            self.mouse_pos.y -= self.cursor.get_height()
            self.dm.update(self.mouse_pos)

    def draw(self):
            self.screen.blit(self.background, (0,0))
            self.dm.draw(self.screen)
            self.screen.blit(self.cursor, (self.mouse_pos.x, 
                                           self.mouse_pos.y))
            pygame.display.update()


if __name__ == "__main__":
    game = DirtGame()
    game.run()
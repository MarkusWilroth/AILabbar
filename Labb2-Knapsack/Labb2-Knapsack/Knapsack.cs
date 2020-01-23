﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Labb2_Knapsack {
    class Knapsack {

        public int maxWeight, totWeight, totValue;
        public List<Item> itemsInKnap;

        public Knapsack(int maxWeight) {
            this.maxWeight = maxWeight;
            totWeight = 0;
            totValue = 0;

            itemsInKnap = new List<Item>();
        }

        public bool AddItem(Item item) {
            if (totWeight + item.itemWeight <= maxWeight) { //Kollar om den får plats
                itemsInKnap.Add(item);
                totWeight += item.itemWeight;
                totValue += item.itemValue;
                return true;
            } else {
                return false;
            }
        }
    }
}

public static void main(String[] args)
{
const fs = require('fs')
fs.readFile('input.txt', (err, inputD) => {
    let output = "";
   if (err) throw err;
      var string = inputD.toString();
      let order = [];
      result = string.split("\n");
      for(var i = 0; i < result.length; i++)
      {
        switch (result[i].split(',').length) {
            case 4:
                if(order.find(order => order.price == result[i].split(',')[1] && order.type == result[i].split(',')[3].replace(/(\r\n|\n|\r)/gm, "")) != undefined)
                {
                    for(let j = 0; j < order.length; j++)
                    {
                        if(order[j].price == result[i].split(',')[1] && order[j].type == result[i].split(',')[3].replace(/(\r\n|\n|\r)/gm, ""))
                        {
                            order[j].size = Number(result[i].split(',')[2]);
                        }
                    }
                }
                else
                {
                    let newprice = {
                        "price": Number(result[i].split(',')[1]),
                        "size": Number(result[i].split(',')[2]),
                        "type": result[i].split(',')[3].replace(/(\r\n|\n|\r)/gm, "")
                    };
                    order.push(newprice);
                }
                break;
            case 2:
                    if(result[i].split(',')[1].replace(/(\r\n|\n|\r)/gm, "") == "best_bid")
                    {
                        output = output + findBestBid(order).price + "," + findBestBid(order).size + "\n";
                    }
                    if(result[i].split(',')[1].replace(/(\r\n|\n|\r)/gm, "") == "best_ask")
                    {
                        output = output + findBestAsk(order).price + "," + findBestAsk(order).size + "\n";
                    }
                break;
            case 3:
                for(let j = 0; j < order.length; j++)
                    {
                        if(result[i].split(',')[1] == "buy" && order[j].price == findBestAsk(order).price && findBestAsk(order).size == order[j].size)
                        {
                            order[j].size = order[j].size - result[i].split(',')[2].replace(/(\r\n|\n|\r)/gm, "");
                        }
                        if(result[i].split(',')[1] == "sell" && order[j].price == findBestBid(order).price && findBestBid(order).size == order[j].size)
                        {
                            order[j].size = order[j].size - result[i].split(',')[2].replace(/(\r\n|\n|\r)/gm, "");
                        }
                        if(result[i].split(',')[1] == "size" && result[i].split(',')[2].replace(/(\r\n|\n|\r)/gm, "") == order[j].price)
                        {
                            output = output + order[j].size + "\n";
                        }
                    }
                break;
      }
      fs.writeFile('Output.txt', output, (err) => {
        if (err) throw err;
    })
    }
})

function findBestBid(order)
{
    let best = {
        "price": 0,
          "size": 0,
          "type": "bid"
    };
    for(let i = 0; i < order.length; i++)
    {
        if(order[i].type == "bid" && order[i].price > best.price && order[i].size != 0) best = order[i];
    }
    return best;
}
function findBestAsk(order)
{
    let best = {
        "price": 1000000000,
          "size": 0,
          "type": "ask"
    };
    let worked = false;
    for(let i = 0; i < order.length; i++)
    {
        if(order[i].type == "ask" && order[i].price < best.price && order[i].size != 0)
        {
            best = order[i];
            worked = true;
        }
    }
    if(worked == false) best.price = 0;
    return best;
}
}
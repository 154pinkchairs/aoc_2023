defmodule Day2 do
def list_possible(game_list) do
  constraints = %{"red" -> 12, "green" -> 13, "blue" -> 14}

  games = File.read!(game_list)
  |> String.split("\n")
  |> Enum.map(fn game_data -> String.split(game_data, ":"))

  games_with_numbers = Enum.map(games, fn game -> Enum.filter(game, fn x -> String.match?(x, ~r/^Game(.+)/) end) end)
end
end
